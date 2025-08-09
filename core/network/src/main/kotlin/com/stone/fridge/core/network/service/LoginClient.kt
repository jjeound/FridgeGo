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
        nickname: String
    ): ApiResponse<String> = loginApi.setNickname(nickname)

    suspend fun setLocation(
        location: Location
    ): ApiResponse<String> = loginApi.setLocation(location)

    suspend fun loginTest(
        email: EmailReq
    ): ApiResponse<Callback> = loginApi.loginTest(email)
}