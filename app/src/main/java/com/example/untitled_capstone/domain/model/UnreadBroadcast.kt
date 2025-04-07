package com.example.untitled_capstone.domain.model

import com.example.untitled_capstone.data.remote.dto.UnreadBroadcastDto

data class UnreadBroadcast(
    val messageId: Long,
    val unreadCount: Int
){
    fun toUnreadBroadcastDto(): UnreadBroadcastDto {
        return UnreadBroadcastDto(
            messageId = messageId,
            unreadCount = unreadCount
        )
    }
}