package com.stone.fridge.domain.repository

import com.stone.fridge.core.util.Resource
import com.stone.fridge.data.remote.dto.ApiResponse
import com.stone.fridge.data.remote.dto.TokenDto

interface TokenRepository {
    suspend fun getAccessToken(): String?

    suspend fun getRefreshToken(): String?

    suspend fun saveAccessToken(accessToken: String)

    suspend fun saveRefreshToken(refreshToken: String)

    suspend fun deleteTokens()

    suspend fun refreshToken(refreshToken: String): Resource<ApiResponse<TokenDto>>

    suspend fun refreshAndSaveToken(): TokenDto?
}