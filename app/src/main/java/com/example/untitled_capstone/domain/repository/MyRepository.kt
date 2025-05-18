package com.example.untitled_capstone.domain.repository

import com.example.untitled_capstone.core.util.Resource
import com.example.untitled_capstone.domain.model.Profile
import kotlinx.coroutines.flow.Flow
import okhttp3.MultipartBody

interface MyRepository {
    suspend fun logout(): Resource<String>
    suspend fun getMyProfile(): Resource<Profile>
    suspend fun getNickname(): Flow<String>
    suspend fun getOtherProfile(name: String): Resource<Profile>
    suspend fun getLocation(): Resource<String>
    suspend fun uploadProfileImage(profileImage: MultipartBody.Part): Resource<String>
    suspend fun repostUser(targetUserId: Long, reportType: String, content: String): Resource<String>
}