package com.example.untitled_capstone.data.remote.service

import com.example.untitled_capstone.data.remote.dto.ApiResponse
import com.example.untitled_capstone.data.remote.dto.TokenDto
import retrofit2.http.Header
import retrofit2.http.POST

interface TokenApi {
    @POST("/api/user/refresh")
    suspend fun refreshToken(
        @Header("Refresh-Token") refreshToken: String
    ): ApiResponse<TokenDto>
}