package com.stone.fridge.domain.model

import com.stone.fridge.data.remote.dto.UnreadBroadcastDto

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