package com.stone.fridge.domain.repository

import com.stone.fridge.data.remote.dto.MessageDto
import com.stone.fridge.data.remote.dto.UnreadBroadcastDto

interface WebSocketRepository {
    fun connect(token: String, roomId: Long, onConnected: () -> Unit, onError: (Throwable) -> Unit)
    suspend fun subscribeRoom(
        roomId: Long,
        onMessage: (MessageDto) -> Unit,
        onUnreadUpdate: (UnreadBroadcastDto) -> Unit
    )
    fun sendMessage(roomId: Long, content: String)
    fun sendReadEvent(roomId: Long)
    fun disconnect()
    fun enterRoom(roomId: Long)
    fun leaveRoom(roomId: Long)
}