package com.example.untitled_capstone.domain.repository

import com.example.untitled_capstone.core.util.Resource
import com.example.untitled_capstone.data.remote.dto.ApiResponse
import com.example.untitled_capstone.domain.model.Profile
import okhttp3.MultipartBody
import okhttp3.RequestBody

interface MyRepository {
    suspend fun logout(): Resource<ApiResponse>

    suspend fun getMyProfile(): Resource<Profile>

    suspend fun getOtherProfile(nickname: String): Resource<Profile>

    suspend fun getLocation(): Resource<String>

    suspend fun uploadProfileImage(profileImage: MultipartBody.Part): Resource<ApiResponse>
}