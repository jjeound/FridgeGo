package com.example.untitled_capstone.data.remote.service

import com.example.untitled_capstone.data.remote.dto.ApiResponse
import com.example.untitled_capstone.data.remote.dto.ProfileResponse
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface MyApi {
    @GET("/api/user/profile")
    suspend fun getProfile(): ProfileResponse

    @GET("/api/user/profile/other")
    suspend fun getOtherProfile(
        @Query("nickname") nickname: String
    ): ProfileResponse

    @POST("/api/user/logout")
    suspend fun logout(): ApiResponse
}