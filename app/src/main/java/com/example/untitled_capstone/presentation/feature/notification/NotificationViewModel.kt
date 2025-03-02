package com.example.untitled_capstone.presentation.feature.notification

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.untitled_capstone.domain.model.Notification
import com.example.untitled_capstone.presentation.feature.notification.state.NotificationState

class NotificationViewModel: ViewModel() {
    var state by mutableStateOf(NotificationState())
        private set

    init {
        state = state.copy(
            notifications = listOf(
                Notification(
                    title = "유통기한",
                    content = "양파의 유통기한이 1일 남았습니다.",
                    time = "time1",
                    isRead = false,
                    navigation = "id1"
                ),
                Notification(
                    title = "title2",
                    content = "content2",
                    time = "time2",
                    isRead = true,
                    navigation = "id1"
                ),
                Notification(
                    title = "title3",
                    content = "content3",
                    time = "time3",
                    isRead = true,
                    navigation = "id1"
                ),
                Notification(
                    title = "title4",
                    content = "content4",
                    time = "time4",
                    isRead = true,
                    navigation = "id1"
                )
            ),
            isLoading = false
        )
    }
}