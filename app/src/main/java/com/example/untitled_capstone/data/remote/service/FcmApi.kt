package com.example.untitled_capstone.data.remote.service

import com.example.untitled_capstone.data.remote.dto.ApiResponse
import retrofit2.http.POST
import retrofit2.http.Query

interface FcmApi {

    @POST("/api/user/fcm-token")
    suspend fun saveFcmToken(
        @Query("token") token: String,
    ): ApiResponse<String>
}