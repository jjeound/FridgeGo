package com.example.untitled_capstone.data.repository

import com.example.untitled_capstone.data.remote.manager.WebSocketManager
import com.example.untitled_capstone.domain.model.Message
import com.example.untitled_capstone.domain.model.UnreadBroadcast
import com.example.untitled_capstone.domain.repository.TokenRepository
import com.example.untitled_capstone.domain.repository.WebSocketRepository
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.runBlocking
import javax.inject.Inject


class WebSocketRepositoryImpl @Inject constructor(
    private val webSocketManager: WebSocketManager,
) : WebSocketRepository {

    override fun connect(token: String, roomId: Long, onConnected: () -> Unit, onError: (Throwable) -> Unit) {
        webSocketManager.connect(token, roomId, onConnected, onError)
    }

    override fun subscribeRoom(
        roomId: Long,
        onMessage: (Message) -> Unit,
        onUnreadUpdate: (UnreadBroadcast) -> Unit
    ) {
        webSocketManager.subscribeRoom(
            roomId = roomId,
            onMessage = { dto -> onMessage(dto.toMessage()) },
            onUnreadUpdate = { dto -> onUnreadUpdate(dto.toUnreadBroadcast()) }
        )
    }

    override fun sendMessage(roomId: Long, content: String) {
        webSocketManager.sendMessage(roomId, content)
    }

    override fun sendReadEvent(roomId: Long) {
        webSocketManager.sendReadEvent(roomId)
    }

    override fun disconnect() {
        webSocketManager.disconnect()
    }
}