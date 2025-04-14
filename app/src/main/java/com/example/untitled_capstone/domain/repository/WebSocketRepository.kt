package com.example.untitled_capstone.domain.repository

import com.example.untitled_capstone.data.remote.dto.MessageDto
import com.example.untitled_capstone.data.remote.dto.UnreadBroadcastDto
import com.example.untitled_capstone.domain.model.UnreadBroadcast

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
}