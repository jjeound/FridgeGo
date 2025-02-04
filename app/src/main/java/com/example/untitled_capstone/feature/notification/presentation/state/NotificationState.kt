package com.example.untitled_capstone.feature.notification.presentation.state

import com.example.untitled_capstone.feature.notification.domain.model.Notification

data class NotificationState(
    val notifications: List<Notification> = emptyList(),
    val isLoading: Boolean = false,
)