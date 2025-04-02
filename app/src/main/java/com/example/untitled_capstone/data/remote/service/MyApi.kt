package com.example.untitled_capstone.data.remote.service

import com.example.untitled_capstone.data.remote.dto.AddressResponse
import com.example.untitled_capstone.data.remote.dto.ApiResponse
import com.example.untitled_capstone.data.remote.dto.ProfileResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Part
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

    @GET("/api/user/location")
    suspend fun getLocation(): AddressResponse

    @Multipart
    @POST("/api/s3/update-profile")
    suspend fun uploadProfileImage(
        @Part profileImage: MultipartBody.Part
    ): ApiResponse
}