package com.example.untitled_capstone.data.remote.service

import com.example.untitled_capstone.data.remote.dto.ApiResponse
import com.example.untitled_capstone.data.remote.dto.LocationDto
import com.example.untitled_capstone.data.remote.dto.ProfileDto
import com.example.untitled_capstone.data.remote.dto.ReportDto
import okhttp3.MultipartBody
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query

interface MyApi {
    @GET("/api/user/profile")
    suspend fun getProfile(): ApiResponse<ProfileDto>

    @GET("/api/user/profile/other")
    suspend fun getOtherProfile(
        @Query("nickname") nickname: String
    ): ApiResponse<ProfileDto>

    @POST("/api/user/logout")
    suspend fun logout(): ApiResponse<String>

    @GET("/api/user/location")
    suspend fun getLocation(): ApiResponse<LocationDto>

    @Multipart
    @POST("/api/s3/update-profile")
    suspend fun uploadProfileImage(
        @Part profileImage: MultipartBody.Part
    ): ApiResponse<String>

    @POST("/api/report/user/{targetUserId}")
        suspend fun reportUser(
        @Path ("targetUserId") targetUserId: Long,
        @Body report: ReportDto
    ): ApiResponse<String>
}