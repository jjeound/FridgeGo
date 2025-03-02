package com.example.untitled_capstone.presentation.feature.notification.state

import com.example.untitled_capstone.domain.model.Notification

data class NotificationState(
    val notifications: List<Notification> = emptyList(),
    val isLoading: Boolean = false,
)