package com.stone.fridge.data.remote.dto

import com.stone.fridge.domain.model.UnreadBroadcast
import kotlinx.serialization.Serializable

@Serializable
data class UnreadBroadcastDto(
    val messageId: Long,
    val unreadCount: Int
) {
    fun toUnreadBroadcast() = UnreadBroadcast(
        messageId = messageId,
        unreadCount = unreadCount
    )
}
