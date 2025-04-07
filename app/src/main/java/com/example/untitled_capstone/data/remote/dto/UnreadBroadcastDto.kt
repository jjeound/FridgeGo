package com.example.untitled_capstone.data.remote.dto

import com.example.untitled_capstone.domain.model.UnreadBroadcast

data class UnreadBroadcastDto(
    val messageId: Long,
    val unreadCount: Int
) {
    fun toUnreadBroadcast() = UnreadBroadcast(
        messageId = messageId,
        unreadCount = unreadCount
    )
}
