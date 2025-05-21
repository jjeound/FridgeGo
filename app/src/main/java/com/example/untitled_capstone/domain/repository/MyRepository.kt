package com.example.untitled_capstone.domain.repository

import androidx.annotation.WorkerThread
import com.example.untitled_capstone.core.util.Resource
import com.example.untitled_capstone.domain.model.Profile
import kotlinx.coroutines.flow.Flow
import okhttp3.MultipartBody

interface MyRepository {
    @WorkerThread
    suspend fun logout(): Flow<Resource<String>>
    @WorkerThread
    suspend fun getMyProfile(): Flow<Resource<Profile>>
    suspend fun getNickname(): String?
    @WorkerThread
    suspend fun getOtherProfile(name: String): Flow<Resource<Profile>>
    @WorkerThread
    suspend fun getLocation(): Flow<Resource<String>>
    @WorkerThread
    suspend fun uploadProfileImage(profileImage: MultipartBody.Part): Flow<Resource<String>>
    @WorkerThread
    suspend fun repostUser(targetUserId: Long, reportType: String, content: String): Flow<Resource<String>>
}