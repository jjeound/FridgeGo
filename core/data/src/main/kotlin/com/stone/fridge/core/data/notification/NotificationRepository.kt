package com.stone.fridge.core.data.notification

import androidx.annotation.WorkerThread
import com.stone.fridge.core.data.util.Resource
import com.stone.fridge.core.model.Notification
import kotlinx.coroutines.flow.Flow

interface NotificationRepository {
    @WorkerThread
    fun getAllNotifications(): Flow<List<Notification>>

    @WorkerThread
    suspend fun readNotification()

    @WorkerThread
    fun getUnreadCount(): Flow<Long>

    @WorkerThread
    fun isUnreadNotification(): Flow<Boolean>
}