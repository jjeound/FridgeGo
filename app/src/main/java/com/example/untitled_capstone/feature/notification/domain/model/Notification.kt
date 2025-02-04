package com.example.untitled_capstone.feature.notification.domain.model

data class Notification(
    val title: String,
    val content: String,
    val time: String,
    val isRead: Boolean,
    val navigation: String
)
