package com.stone.fridge.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.stone.fridge.domain.model.Message
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
){
    fun toMessage(): Message{
        return Message(
            messageId = messageId,
            senderId = senderId,
            senderNickname = senderNickname,
            content = content,
            sentAt = sentAt.let { LocalDateTime.parse(it) },
            unreadCount = unreadCount,
            read = read
        )
    }
}
