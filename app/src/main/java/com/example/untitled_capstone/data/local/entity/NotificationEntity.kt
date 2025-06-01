package com.example.untitled_capstone.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.untitled_capstone.domain.model.Notification

@Entity
data class NotificationEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val title: String,
    val content: String,
    val time: String,
    val isRead: Boolean = false,
){
    fun toDomain(): Notification {
        return Notification(
            title = title,
            content = content,
            time = time,
            isRead = isRead
        )
    }
}

