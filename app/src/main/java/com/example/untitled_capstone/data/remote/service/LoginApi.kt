package com.example.untitled_capstone.data.remote.service

import com.example.untitled_capstone.data.remote.dto.KakaoAccessTokenRequest
import com.example.untitled_capstone.data.remote.dto.ApiResponse
import com.example.untitled_capstone.data.remote.dto.KakaoLoginResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface LoginApi {

    @POST("/api/user/kakao-login")
    suspend fun kakaoLogin(
        @Body accessToken: KakaoAccessTokenRequest
    ): KakaoLoginResponse

    @GET("/api/user/nickname")
    suspend fun setNickname(
        @Query("nickname") nickname: String
    ): ApiResponse


}