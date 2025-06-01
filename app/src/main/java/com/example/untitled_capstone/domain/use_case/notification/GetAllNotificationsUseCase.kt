package com.example.untitled_capstone.domain.use_case.notification

import com.example.untitled_capstone.core.util.Resource
import com.example.untitled_capstone.domain.model.Notification
import com.example.untitled_capstone.domain.repository.NotificationRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetAllNotificationsUseCase @Inject constructor(
    private val repository: NotificationRepository
) {
    operator fun invoke(): Flow<Resource<List<Notification>>> {
        return repository.getAllNotifications()
    }
}