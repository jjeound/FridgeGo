package com.example.untitled_capstone.domain.repository

import androidx.annotation.WorkerThread
import com.example.untitled_capstone.core.util.Resource
import com.example.untitled_capstone.domain.model.AccountInfo
import com.example.untitled_capstone.domain.model.Address
import kotlinx.coroutines.flow.Flow

interface LoginRepository {
    @WorkerThread
    fun kakaoLogin(accessToken: String): Flow<Resource<AccountInfo>>
    @WorkerThread
    fun setNickname(nickname: String): Flow<Resource<String>>
    @WorkerThread
    fun getAddressByCoord(x: String, y: String): Flow<Resource<Address>>
    @WorkerThread
    fun setLocation(district: String, neighborhood: String): Flow<Resource<String>>
    @WorkerThread
    fun modifyNickname(nickname: String): Flow<Resource<String>>
}
