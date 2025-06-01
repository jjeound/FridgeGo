package com.example.untitled_capstone.domain.use_case.notification

import com.example.untitled_capstone.domain.repository.NotificationRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ReadNotificationUseCase @Inject constructor(
    private val repository: NotificationRepository
) {
    suspend operator fun invoke() {
        return repository.readNotification()
    }
}