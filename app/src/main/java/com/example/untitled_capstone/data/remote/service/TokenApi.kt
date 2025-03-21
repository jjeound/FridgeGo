package com.example.untitled_capstone.data.remote.service

import com.example.untitled_capstone.data.remote.dto.RefreshTokenResponse
import retrofit2.http.Header
import retrofit2.http.POST

interface TokenApi {
    @POST("/api/user/refresh")
    suspend fun refreshToken(
        @Header("Refresh-Token") refreshToken: String
    ): RefreshTokenResponse
}