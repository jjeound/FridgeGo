package com.stone.fridge.core.network.service

import com.stone.fridge.core.network.model.ApiResponse
import retrofit2.http.POST
import retrofit2.http.Query

interface FcmApi {

    @POST("/api/user/fcm-token")
    suspend fun saveFcmToken(
        @Query("token") token: String,
    ): ApiResponse<String>
}