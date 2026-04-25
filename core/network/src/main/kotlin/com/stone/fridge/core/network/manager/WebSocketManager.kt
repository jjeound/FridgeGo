package com.stone.fridge.core.network.manager

import android.annotation.SuppressLint
import android.util.Log
import com.google.gson.Gson
import com.stone.fridge.core.model.Message
import com.stone.fridge.core.model.UnreadBroadcast
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import ua.naiksoftware.stomp.Stomp
import ua.naiksoftware.stomp.StompClient
import ua.naiksoftware.stomp.dto.LifecycleEvent
import ua.naiksoftware.stomp.dto.StompHeader
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WebSocketManager @Inject constructor(
    private val gson: Gson,
) {
    private var stompClient: StompClient? = null
    private var isManuallyDisconnected = false
    private var isReconnecting = false
    private var retryCount = 0

    // 재연결 로직을 관리할 전용 스코프 (SupervisorJob으로 하나가 실패해도 다른 작업에 영향 X)
    private val managerScope = CoroutineScope(Dispatchers.IO + SupervisorJob())

    // 구독 리소스를 한꺼번에 해제하기 위한 관리자
    private val compositeDisposable = CompositeDisposable()

    @SuppressLint("CheckResult")
    fun connect(token: String, roomId: Long, onConnected: () -> Unit, onError: (Throwable) -> Unit) {
        if (stompClient?.isConnected == true) return

        isManuallyDisconnected = false

        // 기존 연결 및 구독 정보 초기화
        clearResources()

        stompClient = Stomp.over(Stomp.ConnectionProvider.OKHTTP, "wss://api.refrigerator.asia/ws/chat").apply {
            val headers = listOf(StompHeader("Authorization", "Bearer $token"))

            val lifecycleDisposable = lifecycle().subscribe { event ->
                when (event.type) {
                    LifecycleEvent.Type.OPENED -> {
                        Log.d("WebSocketManager", "STOMP Connection Opened")
                        isReconnecting = false
                        retryCount = 0 // 성공 시 재시도 횟수 초기화
                        enterRoom(roomId)
                        onConnected()
                    }

                    LifecycleEvent.Type.ERROR -> {
                        Log.e("WebSocketManager", "STOMP Error", event.exception)
                        handleRetry(token, roomId, onConnected, onError)
                        onError(event.exception)
                    }

                    LifecycleEvent.Type.CLOSED -> {
                        Log.d("WebSocketManager", "STOMP Connection Closed")
                        if (!isManuallyDisconnected) {
                            handleRetry(token, roomId, onConnected, onError)
                        }
                    }
                    else -> {}
                }
            }
            compositeDisposable.add(lifecycleDisposable)
            connect(headers)
        }
    }

    private fun handleRetry(token: String, roomId: Long, onConnected: () -> Unit, onError: (Throwable) -> Unit) {
        if (isReconnecting) return
        isReconnecting = true

        managerScope.launch {
            // 지수 백오프: 5초, 10초, 20초... 최대 60초까지
            val delayTime = (5000L * (1 shl (retryCount.coerceAtMost(4)))).coerceAtMost(60000L)
            Log.d("WebSocketManager", "Retrying connection in ${delayTime/1000}s... (Attempt: ${retryCount + 1})")

            delay(delayTime)
            retryCount++
            connect(token, roomId, onConnected, onError)
        }
    }

    fun disconnect() {
        isManuallyDisconnected = true
        stompClient?.disconnect()
        clearResources()
        Log.d("WebSocketManager", "Disconnected manually")
    }

    @SuppressLint("CheckResult")
    fun subscribeRoom(roomId: Long, onMessage: (Message) -> Unit, onUnreadUpdate: (UnreadBroadcast) -> Unit) {
        val messageSub = stompClient?.topic("/sub/chat/room/$roomId")?.subscribe({ stompMessage ->
            val message = gson.fromJson(stompMessage.payload, Message::class.java)
            onMessage(message)
        }, { error ->
            Log.e("WebSocketManager", "Msg Sub Error: ${error.message}")
        })

        val unreadSub = stompClient?.topic("/sub/chat/room/$roomId/unread")?.subscribe({ stompMessage ->
            val unread = gson.fromJson(stompMessage.payload, UnreadBroadcast::class.java)
            onUnreadUpdate(unread)
        }, { error ->
            Log.e("WebSocketManager", "Unread Sub Error: ${error.message}")
        })

        messageSub?.let { compositeDisposable.add(it) }
        unreadSub?.let { compositeDisposable.add(it) }
    }

    fun sendMessage(roomId: Long, content: String) {
        val payload = gson.toJson(mapOf("roomId" to roomId, "content" to content))
        sendData("/pub/chat/message", payload)
    }

    fun sendReadEvent(roomId: Long) {
        val payload = gson.toJson(mapOf("roomId" to roomId))
        sendData("/pub/chat/read", payload)
    }

    private fun sendData(destination: String, payload: String) {
        val disposable = stompClient?.send(destination, payload)?.subscribe({
            Log.d("WebSocketManager", "Sent to $destination")
        }, { error ->
            Log.e("WebSocketManager", "Send Error to $destination: ${error.message}")
        })
        disposable?.let { compositeDisposable.add(it) }
    }

    private fun enterRoom(roomId: Long) {
        val payload = gson.toJson(mapOf("roomId" to roomId))
        sendData("/pub/chat/enter", payload)
    }

    fun leaveRoom(roomId: Long) {
        val payload = gson.toJson(mapOf("roomId" to roomId))
        sendData("/pub/chat/leave", payload)
        disconnect()
    }

    private fun clearResources() {
        compositeDisposable.clear() // 모든 구독 해제
        isReconnecting = false
    }

    // ViewModel이나 App 종료 시 호출하여 코루틴 취소
    fun onDestroy() {
        managerScope.cancel()
        disconnect()
    }
}