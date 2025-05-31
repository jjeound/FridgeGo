package com.example.untitled_capstone.data.repository

import com.example.untitled_capstone.data.AppDispatchers
import com.example.untitled_capstone.data.Dispatcher
import com.example.untitled_capstone.data.local.db.MessageItemDatabase
import com.example.untitled_capstone.data.remote.dto.MessageDto
import com.example.untitled_capstone.data.remote.dto.UnreadBroadcastDto
import com.example.untitled_capstone.data.remote.manager.WebSocketManager
import com.example.untitled_capstone.domain.repository.WebSocketRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject


class WebSocketRepositoryImpl @Inject constructor(
    private val webSocketManager: WebSocketManager,
    private val db: MessageItemDatabase,
    @Dispatcher(AppDispatchers.IO) private val ioDispatcher: CoroutineDispatcher,
) : WebSocketRepository {

    override fun connect(token: String, roomId: Long, onConnected: () -> Unit, onError: (Throwable) -> Unit) {
        webSocketManager.connect(token, roomId, onConnected, onError)
    }

    override suspend fun subscribeRoom(
        roomId: Long,
        onMessage: (MessageDto) -> Unit,
        onUnreadUpdate: (UnreadBroadcastDto) -> Unit
    ) {
        webSocketManager.subscribeRoom(
            roomId = roomId,
            onMessage = { dto ->
                CoroutineScope(ioDispatcher).launch {
                    saveMessageToDatabase(dto, roomId)
                }
                onMessage(dto) },
            onUnreadUpdate = { dto ->
                CoroutineScope(ioDispatcher).launch {
                    updateUnreadCount(
                        messageId = dto.messageId,
                        roomId = roomId,
                        unreadCount = dto.unreadCount
                    )
                }
                onUnreadUpdate(dto)
            }
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

    override fun enterRoom(roomId: Long) {
        webSocketManager.enterRoom(roomId)
    }

    override fun leaveRoom(roomId: Long) {
        webSocketManager.leaveRoom(roomId)
    }

    private suspend fun saveMessageToDatabase(message: MessageDto, roomId: Long) {
        db.dao.insert(message.toMessageEntity(roomId))
    }

    private suspend fun updateUnreadCount(messageId: Long, roomId: Long, unreadCount: Int) {
        db.dao.updateUnreadCount(messageId, roomId, unreadCount)
    }
}