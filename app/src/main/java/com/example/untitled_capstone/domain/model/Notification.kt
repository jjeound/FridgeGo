package com.example.untitled_capstone.domain.model

import com.example.untitled_capstone.data.local.entity.NotificationEntity

data class Notification(
    val title: String,
    val content: String,
    val time: String,
    val isRead: Boolean,
){
    fun toEntity(): NotificationEntity{
        return NotificationEntity(
            title = title,
            content = content,
            time = time,
            isRead = isRead
        )
    }
}
