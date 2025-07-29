package com.stone.fridge.data.remote.dto

import com.stone.fridge.domain.model.ChattingRoomRaw
import kotlinx.serialization.Serializable
import java.time.LocalDateTime

@Serializable
data class ChatRoomRawDto(
    val active: Boolean,
    val currentParticipants: Int,
    val lastMessage: String? = null,
    val lastMessageTime: String? = null,
    val name: String,
    val roomId: Long,
    val unreadCount: Int,
    val createdAt: String,
    val host: Boolean,
){
    fun toChattingRoomRaw(): ChattingRoomRaw {
        return ChattingRoomRaw(
            active = active,
            currentParticipants = currentParticipants,
            lastMessage = lastMessage,
            lastMessageTime = lastMessageTime?.let { LocalDateTime.parse(it) },
            name = name,
            roomId = roomId,
            unreadCount = unreadCount,
            createdAt = createdAt.let { LocalDateTime.parse(it) },
            host = host
        )
    }
}