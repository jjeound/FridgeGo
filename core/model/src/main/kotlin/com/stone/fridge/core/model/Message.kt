package com.stone.fridge.core.model

import kotlinx.serialization.Serializable

@Serializable
data class Message(
    val content: String,
    val senderId: Long,
    val messageId: Long,
    val read: Boolean,
    val senderNickname: String,
    val sentAt: String,
    val unreadCount: Int
)