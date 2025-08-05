package com.stone.fridge.core.network.service

import com.stone.fridge.core.model.Callback
import com.stone.fridge.core.model.EmailReq
import com.stone.fridge.core.model.KakaoAccessTokenRequest
import com.stone.fridge.core.model.Location
import com.stone.fridge.core.network.model.ApiResponse
import javax.inject.Inject

class LoginClient @Inject constructor(
    private val loginApi: LoginApi,
) {
    suspend fun kakaoLogin(
        accessToken: KakaoAccessTokenRequest
    ): ApiResponse<Callback> = loginApi.kakaoLogin(accessToken)

    suspend fun setNickname(
        token: String,
        nickname: String
    ): ApiResponse<String> = loginApi.setNickname(token, nickname)

    suspend fun setLocation(
        token: String,
        location: Location
    ): ApiResponse<String> = loginApi.setLocation(token, location)

    suspend fun loginTest(
        email: EmailReq
    ): ApiResponse<Callback> = loginApi.loginTest(email)
}