package com.example.untitled_capstone.domain.model

import java.time.LocalDateTime

data class Message(
    val content: String,
    val senderId: Long,
    val messageId: Long,
    val read: Boolean,
    val senderNickname: String,
    val sentAt: LocalDateTime,
    val unreadCount: Int
)