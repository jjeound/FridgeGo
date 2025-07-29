package com.stone.fridge.domain.repository

import androidx.annotation.WorkerThread
import com.stone.fridge.core.util.Resource
import com.stone.fridge.domain.model.Profile
import kotlinx.coroutines.flow.Flow
import java.io.File

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
    fun uploadProfileImage(profileImage: File): Flow<Resource<String>>
    @WorkerThread
    fun repostUser(targetUserId: Long, reportType: String, content: String): Flow<Resource<String>>
    @WorkerThread
    fun deleteProfileImage(): Flow<Resource<String>>
    @WorkerThread
    fun modifyNickname(nickname: String): Flow<Resource<String>>
    @WorkerThread
    fun deleteUser(): Flow<Resource<String>>
}