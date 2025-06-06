package com.stone.fridge.domain.use_case.notification

import com.stone.fridge.domain.repository.NotificationRepository
import javax.inject.Inject

class IsUnreadNotificationUseCase @Inject constructor(
    private val repository: NotificationRepository
) {
    operator fun invoke() = repository.isUnreadNotification()
}