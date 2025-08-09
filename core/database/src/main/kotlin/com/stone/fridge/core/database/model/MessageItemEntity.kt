package com.stone.fridge.core.database.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.stone.fridge.core.model.Message
import java.time.LocalDateTime

@Entity
data class MessageItemEntity(
    @PrimaryKey val messageId: Long,
    val roomId: Long,
    val senderId: Long,
    val senderNickname: String,
    val content: String,
    val sentAt: String,
    val unreadCount: Int,
    val read: Boolean
)
fun MessageItemEntity.toDomain(): Message {
    return Message(
        messageId = messageId,
        senderId = senderId,
        senderNickname = senderNickname,
        content = content,
        sentAt = sentAt,
        unreadCount = unreadCount,
        read = read
    )
}
fun Message.toEntity(roomId: Long): MessageItemEntity {
    return MessageItemEntity(
        messageId = messageId,
        roomId = roomId,
        senderId = senderId,
        senderNickname = senderNickname,
        content = content,
        sentAt = sentAt,
        unreadCount = unreadCount,
        read = read
    )
}
