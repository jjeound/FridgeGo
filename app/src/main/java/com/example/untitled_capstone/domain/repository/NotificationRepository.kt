package com.example.untitled_capstone.domain.repository

import androidx.annotation.WorkerThread
import com.example.untitled_capstone.core.util.Resource
import com.example.untitled_capstone.domain.model.Notification
import kotlinx.coroutines.flow.Flow

interface NotificationRepository {
    @WorkerThread
    fun getAllNotifications(): Flow<Resource<List<Notification>>>

    @WorkerThread
    suspend fun readNotification()

    @WorkerThread
    fun insertNotification(notification: Notification): Flow<Unit>
}