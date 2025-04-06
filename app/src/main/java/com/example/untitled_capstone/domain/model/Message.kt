package com.example.untitled_capstone.domain.model

data class Message(
    val content: String,
    val enderId: Long,
    val messageId: Long,
    val read: Boolean,
    val senderNickname: String,
    val sentAt: String,
    val unreadCount: Int
)