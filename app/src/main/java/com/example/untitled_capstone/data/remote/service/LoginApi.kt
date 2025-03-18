package com.example.untitled_capstone.data.remote.service

import com.example.untitled_capstone.data.remote.dto.KakaoLoginResponse
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface LoginApi {

    @GET("/callback")
    suspend fun kakaoLogin(
        @Query("code") code: String
    ): KakaoLoginResponse

    @POST("/whoami")
    suspend fun whoami()
}