package com.example.untitled_capstone.domain.repository

import com.example.untitled_capstone.core.util.Resource
import com.example.untitled_capstone.data.remote.dto.ApiResponse
import com.example.untitled_capstone.data.remote.dto.TokenDto

interface TokenRepository {
    suspend fun getAccessToken(): String?

    suspend fun getRefreshToken(): String?

    suspend fun saveAccessToken(accessToken: String)

    suspend fun saveRefreshToken(refreshToken: String)

    suspend fun deleteTokens()

    suspend fun refreshToken(refreshToken: String): Resource<ApiResponse<TokenDto>>

    suspend fun refreshAndSaveToken(): TokenDto?
}