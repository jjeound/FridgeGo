package com.stone.fridge.data.remote.dto

import com.stone.fridge.data.local.entity.MessageItemEntity
import com.stone.fridge.domain.model.Message
import java.time.LocalDateTime

data class MessageDto(
    val content: String,
    val enderId: Long,
    val messageId: Long,
    val read: Boolean,
    val senderNickname: String,
    val sentAt: String,
    val unreadCount: Int
){
    fun toMessageEntity(roomId: Long) = MessageItemEntity(
        messageId = messageId,
        roomId = roomId,
        senderId = enderId,
        senderNickname = senderNickname,
        content = content,
        sentAt = sentAt,
        unreadCount = unreadCount,
        read = read
    )
    fun toMessage() = Message(
        content = content,
        senderId = enderId,
        messageId = messageId,
        read = read,
        senderNickname = senderNickname,
        sentAt = sentAt.let { LocalDateTime.parse(it) },
        unreadCount = unreadCount
    )
}