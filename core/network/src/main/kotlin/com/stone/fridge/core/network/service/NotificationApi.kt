package com.stone.fridge.core.network.service

import com.stone.fridge.core.model.Notification
import com.stone.fridge.core.network.model.ApiResponse
import retrofit2.http.GET
import retrofit2.http.POST

interface NotificationApi {
    @POST("/api/notifications/{id}/read")
    suspend fun readAll(): ApiResponse<String>

    @GET("/api/notifications")
    suspend fun getNotifications(): ApiResponse<List<Notification>>

    @GET("/api/notifications/unread-count")
    suspend fun getUnreadCount(): ApiResponse<Long>
}