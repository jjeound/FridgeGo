package com.example.untitled_capstone.domain.repository

import androidx.annotation.WorkerThread
import com.example.untitled_capstone.core.util.Resource
import com.example.untitled_capstone.domain.model.Profile
import kotlinx.coroutines.flow.Flow
import okhttp3.MultipartBody

interface MyRepository {
    @WorkerThread
    fun logout(): Flow<Resource<String>>
    @WorkerThread
    fun getMyProfile(): Flow<Resource<Profile>>
    suspend fun getNickname(): String?
    @WorkerThread
    fun getOtherProfile(nickname: String): Flow<Resource<Profile>>
    @WorkerThread
    fun getLocation(): Flow<Resource<String>>
    @WorkerThread
    fun uploadProfileImage(profileImage: MultipartBody.Part): Flow<Resource<String>>
    @WorkerThread
    fun repostUser(targetUserId: Long, reportType: String, content: String): Flow<Resource<String>>
}