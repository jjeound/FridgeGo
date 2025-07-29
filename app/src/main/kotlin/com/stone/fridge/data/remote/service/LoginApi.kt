package com.stone.fridge.data.remote.service

import com.stone.fridge.data.remote.dto.KakaoAccessTokenRequest
import com.stone.fridge.data.remote.dto.ApiResponse
import com.stone.fridge.data.remote.dto.CallbackDto
import com.stone.fridge.data.remote.dto.EmailReq
import com.stone.fridge.data.remote.dto.LocationDto
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Query

interface LoginApi {

    @POST("/api/user/kakao-login")
    suspend fun kakaoLogin(
        @Body accessToken: KakaoAccessTokenRequest
    ): ApiResponse<CallbackDto>

    @POST("/api/user/nickname")
    suspend fun setNickname(
        @Header("Authorization") token: String,
        @Query("nickname") nickname: String
    ): ApiResponse<String>

    @POST("/api/user/location")
    suspend fun setLocation(
        @Header("Authorization") token: String,
        @Body location: LocationDto
    ): ApiResponse<String>

    @POST("/api/user/login")
    suspend fun loginTest(
        @Body email: EmailReq
    ): ApiResponse<CallbackDto>
}