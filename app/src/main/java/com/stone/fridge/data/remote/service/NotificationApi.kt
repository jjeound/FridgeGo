package com.stone.fridge.data.remote.service

import com.stone.fridge.data.remote.dto.ApiResponse
import com.stone.fridge.data.remote.dto.NotificationDto
import retrofit2.http.GET
import retrofit2.http.POST

interface NotificationApi {
    @POST("/api/notifications/{id}/read")
    suspend fun readAll(): ApiResponse<String>

    @GET("/api/notifications")
    suspend fun getNotifications(): ApiResponse<List<NotificationDto>>

    @GET("/api/notifications/unread-count")
    suspend fun getUnreadCount(): ApiResponse<Long>
}