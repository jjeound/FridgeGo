package com.stone.fridge.core.network.service

import com.stone.fridge.core.model.Token
import com.stone.fridge.core.network.model.ApiResponse
import javax.inject.Inject

class TokenClient @Inject constructor(
    private val tokenApi: TokenApi,
) {
    suspend fun refreshToken(
        refreshToken: String
    ): ApiResponse<Token> = tokenApi.refreshToken(refreshToken)
}