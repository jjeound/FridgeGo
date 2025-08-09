package com.stone.fridge.core.network.service

import com.stone.fridge.core.model.Callback
import com.stone.fridge.core.model.EmailReq
import com.stone.fridge.core.model.KakaoAccessTokenRequest
import com.stone.fridge.core.model.Location
import com.stone.fridge.core.network.model.ApiResponse
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Query

interface LoginApi {

    @POST("/api/user/kakao-login")
    suspend fun kakaoLogin(
        @Body accessToken: KakaoAccessTokenRequest
    ): ApiResponse<Callback>

    @POST("/api/user/nickname")
    suspend fun setNickname(
        @Query("nickname") nickname: String
    ): ApiResponse<String>

    @POST("/api/user/location")
    suspend fun setLocation(
        @Body location: Location
    ): ApiResponse<String>

    @POST("/api/user/login")
    suspend fun loginTest(
        @Body email: EmailReq
    ): ApiResponse<Callback>
}