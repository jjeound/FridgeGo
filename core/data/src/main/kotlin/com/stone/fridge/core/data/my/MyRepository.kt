package com.stone.fridge.core.data.my

import androidx.annotation.WorkerThread
import com.stone.fridge.core.model.Profile
import kotlinx.coroutines.flow.Flow
import java.io.File

interface MyRepository {
    @WorkerThread
    fun logout(): Flow<String>
    @WorkerThread
    fun getMyProfile(): Flow<Profile>
    @WorkerThread
    fun getOtherProfile(nickname: String): Flow<Profile>
    @WorkerThread
    fun uploadProfileImage(profileImage: File): Flow<String>
    @WorkerThread
    fun repostUser(targetUserId: Long, reportType: String, content: String): Flow<String>
    @WorkerThread
    fun deleteProfileImage(): Flow<String>
    @WorkerThread
    fun modifyNickname(nickname: String): Flow<String>
    @WorkerThread
    fun deleteUser(): Flow<String>
}