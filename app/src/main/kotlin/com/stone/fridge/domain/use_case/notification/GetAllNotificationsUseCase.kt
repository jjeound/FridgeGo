package com.stone.fridge.domain.use_case.notification

import com.stone.fridge.core.util.Resource
import com.stone.fridge.domain.model.Notification
import com.stone.fridge.domain.repository.NotificationRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetAllNotificationsUseCase @Inject constructor(
    private val repository: NotificationRepository
) {
    operator fun invoke(): Flow<Resource<List<Notification>>> {
        return repository.getAllNotifications()
    }
}