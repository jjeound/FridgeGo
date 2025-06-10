package com.stone.fridge.data.remote.dto

import com.stone.fridge.domain.model.Notification
import kotlinx.serialization.Serializable
import java.time.LocalDateTime

@Serializable
data class NotificationDto(
    val id: Long,
    val ingredientName: String,
    val content: String?,
    val scheduledAt: String,
    val status: String,
    val read: Boolean
){
    fun toDomain(): Notification{
        return Notification(
            title = ingredientName,
            content = content,
            time = LocalDateTime.parse(scheduledAt),
            isRead = read
        )
    }
}
