package com.stone.fridge.data.remote.dto

import com.stone.fridge.domain.model.Notification
import java.time.LocalDateTime

data class NotificationDto(
    val id: Long,
    val ingredientName: String,
    val scheduledAt: String,
    val status: String,
    val read: Boolean
){
    fun toDomain(): Notification{
        return Notification(
            title = ingredientName,
            time = LocalDateTime.parse(scheduledAt),
            isRead = read
        )
    }
}
