package com.example.untitled_capstone.data.remote.service

import com.example.untitled_capstone.data.remote.dto.AddressResponse
import com.example.untitled_capstone.data.remote.dto.KakaoAccessTokenRequest
import com.example.untitled_capstone.data.remote.dto.ApiResponse
import com.example.untitled_capstone.data.remote.dto.EmailReq
import com.example.untitled_capstone.data.remote.dto.KakaoLoginResponse
import com.example.untitled_capstone.data.remote.dto.LocationDto
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Query

interface LoginApi {

    @POST("/api/user/kakao-login")
    suspend fun kakaoLogin(
        @Body accessToken: KakaoAccessTokenRequest
    ): KakaoLoginResponse

    @POST("/api/user/nickname")
    suspend fun setNickname(
        @Header("Authorization") token: String,
        @Query("nickname") nickname: String
    ): ApiResponse

    @POST("/api/user/location")
    suspend fun setLocation(
        @Header("Authorization") token: String,
        @Body location: LocationDto
    ): ApiResponse

    @PATCH("/api/user/nickname")
    suspend fun modifyNickname(
        @Header("Authorization") token: String,
        @Query("nickname") nickname: String
    ): ApiResponse

    @POST("/api/user/login")
    suspend fun loginTest(
        @Body email: EmailReq
    ): KakaoLoginResponse

}