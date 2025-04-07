package com.example.untitled_capstone.domain.model

import com.example.untitled_capstone.data.remote.dto.MessageDto

data class Message(
    val content: String,
    val enderId: Long,
    val messageId: Long,
    val read: Boolean,
    val senderNickname: String,
    val sentAt: String,
    val unreadCount: Int
){
    fun toMessageDto(): MessageDto {
        return MessageDto(
            content = content,
            enderId = enderId,
            messageId = messageId,
            read = read,
            senderNickname = senderNickname,
            sentAt = sentAt,
            unreadCount = unreadCount
        )
    }
}