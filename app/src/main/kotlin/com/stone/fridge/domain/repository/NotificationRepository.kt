package com.stone.fridge.domain.repository

import androidx.annotation.WorkerThread
import com.stone.fridge.core.util.Resource
import com.stone.fridge.domain.model.Notification
import kotlinx.coroutines.flow.Flow

interface NotificationRepository {
    @WorkerThread
    fun getAllNotifications(): Flow<Resource<List<Notification>>>

    @WorkerThread
    fun readNotification(): Flow<Unit>

    @WorkerThread
    fun getUnreadCount(): Flow<Resource<Long>>

    @WorkerThread
    fun isUnreadNotification(): Flow<Boolean>
}