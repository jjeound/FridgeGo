package com.example.untitled_capstone.domain.repository

import kotlinx.coroutines.flow.Flow


interface TokenManager {
    fun getAccessToken(): Flow<String?>

    fun getRefreshToken(): Flow<String?>

    suspend fun saveAccessToken(accessToken: String)

    suspend fun saveRefreshToken(refreshToken: String)

    suspend fun deleteTokens()
}