package com.stone.fridge.data.remote.service

import com.stone.fridge.data.remote.dto.ApiResponse
import com.stone.fridge.data.remote.dto.TokenDto
import retrofit2.http.Header
import retrofit2.http.POST

interface TokenApi {
    @POST("/api/user/refresh")
    suspend fun refreshToken(
        @Header("Refresh-Token") refreshToken: String
    ): ApiResponse<TokenDto>
}