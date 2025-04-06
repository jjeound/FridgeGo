package com.example.untitled_capstone.data.remote.dto

import com.example.untitled_capstone.domain.model.ChattingRoomRaw

data class ChatRoomRawDto(
    val active: Boolean,
    val currentParticipants: Int,
    val lastMessage: String? = null,
    val lastMessageTime: String? = null,
    val name: String,
    val roomId: Long,
    val unreadCount: Int
){
    fun toChattingRoomRaw(): ChattingRoomRaw {
        return ChattingRoomRaw(
            active = active,
            currentParticipants = currentParticipants,
            lastMessage = lastMessage,
            lastMessageTime = lastMessageTime,
            name = name,
            roomId = roomId,
            unreadCount = unreadCount
        )
    }
}