package com.stone.fridge.core.model

import kotlinx.serialization.Serializable

@Serializable
data class UnreadBroadcast(
    val messageId: Long,
    val unreadCount: Int
)
