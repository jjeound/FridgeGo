package com.stone.fridge.core.network.manager

import android.annotation.SuppressLint
import android.util.Log
import com.google.gson.Gson
import com.stone.fridge.core.model.Message
import com.stone.fridge.core.model.UnreadBroadcast
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import ua.naiksoftware.stomp.Stomp
import ua.naiksoftware.stomp.StompClient
import ua.naiksoftware.stomp.dto.LifecycleEvent
import ua.naiksoftware.stomp.dto.StompHeader
import javax.inject.Inject

class WebSocketManager @Inject constructor(
    private val gson: Gson,
) {
    private var stompClient: StompClient? = null
    private var isManuallyDisconnected = false
    private var isReconnecting = false

    @SuppressLint("CheckResult")
    fun connect(token: String, roomId: Long, onConnected: () -> Unit, onError: (Throwable) -> Unit) {
        if (stompClient?.isConnected == true) return
        isManuallyDisconnected = false

        leaveRoom(roomId) // 기존 연결 정리

        stompClient = Stomp.over(Stomp.ConnectionProvider.OKHTTP, "wss://api.refrigerator.asia/ws/chat").apply {
            val headers = listOf(StompHeader("Authorization", "Bearer $token"))

            lifecycle().subscribe { event ->
                when (event.type) {
                    LifecycleEvent.Type.OPENED -> {
                        isReconnecting = false
                        enterRoom(roomId)
                        onConnected()
                    }

                    LifecycleEvent.Type.ERROR -> {
                        if (!isReconnecting) {
                            isReconnecting = true
                            retryConnect(token, roomId, onConnected, onError)
                        }
                        onError(event.exception)
                    }

                    LifecycleEvent.Type.CLOSED -> {
                        if (!isManuallyDisconnected && !isReconnecting) { // 자동 재연결 시도
                            isReconnecting = true
                            retryConnect(token, roomId, onConnected, onError)
                        }
                    }

                    else -> {}
                }
            }
            connect(headers)
        }
    }

    fun disconnect() {
        isManuallyDisconnected = true // 수동으로 연결을 끊었음을 표시
        stompClient?.disconnect()
        stompClient = null
    }

    @SuppressLint("CheckResult")
    fun subscribeRoom(roomId: Long, onMessage: (Message) -> Unit, onUnreadUpdate: (UnreadBroadcast) -> Unit) {
            stompClient?.topic("/sub/chat/room/$roomId")?.subscribe ({ stompMessage ->
                val message = gson.fromJson(stompMessage.payload, Message::class.java)
                Log.d("onMessage", "Received message: $message")
                onMessage(message)
            }, { error ->
                Log.e("WebSocketManager", "Error subscribing to room $roomId: ${error.message}")
            })

            stompClient?.topic("/sub/chat/room/$roomId/unread")?.subscribe ({ stompMessage ->
                val unread = gson.fromJson(stompMessage.payload, UnreadBroadcast::class.java)
                Log.d("onUnreadUpdate", "Received unread update: $unread")
                onUnreadUpdate(unread)
            }, { error ->
                Log.e("WebSocketManager", "Error subscribing to room $roomId: ${error.message}")
            })
        }

    @SuppressLint("CheckResult")
    fun sendMessage(roomId: Long, content: String) {
        val payload = gson.toJson(mapOf("roomId" to roomId, "content" to content))
        stompClient?.send("/pub/chat/message", payload)?.subscribe({
            Log.d("WebSocketManager", "Message sent successfully")
        }, { error ->
            Log.e("WebSocketManager", "Error sending message: ${error.message}")
        })
    }

    @SuppressLint("CheckResult")
    fun sendReadEvent(roomId: Long) {
        val payload = gson.toJson(mapOf("roomId" to roomId))
        stompClient?.send("/pub/chat/read", payload)?.subscribe({
            Log.d("WebSocketManager", "Read event sent successfully")
        }, { error ->
            Log.e("WebSocketManager", "Error sending read event: ${error.message}")
        })
    }

    private fun retryConnect(
        token: String, roomId: Long, onConnected: () -> Unit, onError: (Throwable) -> Unit
    ) {
        CoroutineScope(Dispatchers.IO).launch {
            delay(5000) // 5초 간격으로 재시도
            connect(token, roomId, onConnected, onError)
        }
    }

    @SuppressLint("CheckResult")
    fun enterRoom(roomId: Long) {
        val payload = gson.toJson(mapOf("roomId" to roomId))
        stompClient?.send("/pub/chat/enter", payload)?.subscribe(
            { Log.d("WebSocketManager", "Entered room $roomId successfully") },
            { error -> Log.e("WebSocketManager", "Error entering room $roomId: ${error.message}") }
        )
    }

    @SuppressLint("CheckResult")
    fun leaveRoom(roomId: Long) {
        val payload = gson.toJson(mapOf("roomId" to roomId))
        stompClient?.send("/pub/chat/leave", payload)?.subscribe({
            Log.d("WebSocketManager", "Left room $roomId successfully")
        }, { error ->
            Log.e("WebSocketManager", "Error leaving room $roomId: ${error.message}")
        })
        disconnect()
    }

}