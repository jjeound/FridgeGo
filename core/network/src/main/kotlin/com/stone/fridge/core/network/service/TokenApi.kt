package com.stone.fridge.core.network.service

import com.stone.fridge.core.model.Token
import com.stone.fridge.core.network.model.ApiResponse
import retrofit2.http.Header
import retrofit2.http.POST

interface TokenApi {
    @POST("/api/user/refresh")
    suspend fun refreshToken(
        @Header("Refresh-Token") refreshToken: String
    ): ApiResponse<Token>
}