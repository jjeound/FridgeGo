package com.stone.fridge.core.network.service

import com.stone.fridge.core.network.model.ApiResponse
import javax.inject.Inject

class FcmClient @Inject constructor(
    private val fcmApi: FcmApi,
) {
    suspend fun saveFcmToken(
        token: String,
    ): ApiResponse<String> = fcmApi.saveFcmToken(token)
}