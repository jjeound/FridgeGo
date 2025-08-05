package com.stone.fridge.core.network.service

import com.stone.fridge.core.model.Notification
import com.stone.fridge.core.network.model.ApiResponse
import javax.inject.Inject

class NotificationClient @Inject constructor(
    private val notificationApi: NotificationApi,
) {
    suspend fun readAll(): ApiResponse<String> =
        notificationApi.readAll()

    suspend fun getNotifications(): ApiResponse<List<Notification>> =
        notificationApi.getNotifications()

    suspend fun getUnreadCount(): ApiResponse<Long> =
        notificationApi.getUnreadCount()
}