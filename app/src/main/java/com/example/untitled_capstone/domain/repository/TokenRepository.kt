package com.example.untitled_capstone.domain.repository

import com.example.untitled_capstone.core.util.Resource
import com.example.untitled_capstone.data.remote.dto.ApiResponse
import com.example.untitled_capstone.data.remote.dto.TokenDto
import kotlinx.coroutines.flow.Flow

interface TokenRepository {
    fun getAccessToken(): Flow<String?>

    fun getRefreshToken(): Flow<String?>

    suspend fun saveAccessToken(accessToken: String)

    suspend fun saveRefreshToken(refreshToken: String)

    suspend fun deleteTokens()

    suspend fun refreshToken(refreshToken: String): Resource<ApiResponse<TokenDto>>
}