package com.example.untitled_capstone.domain.repository

import androidx.annotation.WorkerThread
import com.example.untitled_capstone.core.util.Resource
import com.example.untitled_capstone.domain.model.AccountInfo
import com.example.untitled_capstone.domain.model.Address
import kotlinx.coroutines.flow.Flow

interface LoginRepository {
    @WorkerThread
    suspend fun kakaoLogin(accessToken: String): Flow<Resource<AccountInfo>>
    @WorkerThread
    suspend fun setNickname(nickname: String): Flow<Resource<String>>
    @WorkerThread
    suspend fun getAddressByCoord(x: String, y: String): Flow<Resource<Address>>
    @WorkerThread
    suspend fun setLocation(district: String, neighborhood: String): Flow<Resource<String>>
    @WorkerThread
    suspend fun modifyNickname(nickname: String): Flow<Resource<String>>
}
