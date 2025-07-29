package com.stone.fridge.domain.repository

import com.stone.fridge.data.remote.dto.UnreadBroadcastDto

interface WebSocketRepository {
    fun connect(token: String, roomId: Long, onConnected: () -> Unit, onError: (Throwable) -> Unit)
    suspend fun subscribeRoom(
        roomId: Long,
        onUnreadUpdate: (UnreadBroadcastDto) -> Unit
    )
    fun sendMessage(roomId: Long, content: String)
    fun sendReadEvent(roomId: Long)
    fun leaveRoom(roomId: Long)
}