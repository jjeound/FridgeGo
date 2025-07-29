package com.stone.fridge.domain.model

import java.time.LocalDateTime


data class Notification(
    val title: String,
    val content: String?,
    val time: LocalDateTime,
    val isRead: Boolean,
)
