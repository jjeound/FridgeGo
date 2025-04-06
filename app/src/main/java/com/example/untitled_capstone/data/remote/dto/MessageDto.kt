package com.example.untitled_capstone.data.remote.dto

import com.example.untitled_capstone.domain.model.Message

data class MessageDto(
    val content: String,
    val enderId: Long,
    val messageId: Long,
    val read: Boolean,
    val senderNickname: String,
    val sentAt: String,
    val unreadCount: Int
){
    fun toMessage() = Message(
        content = content,
        enderId = enderId,
        messageId = messageId,
        read = read,
        senderNickname = senderNickname,
        sentAt = sentAt,
        unreadCount = unreadCount
    )
}