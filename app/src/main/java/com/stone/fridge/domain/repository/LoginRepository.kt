package com.stone.fridge.domain.repository

import androidx.annotation.WorkerThread
import com.stone.fridge.core.util.Resource
import com.stone.fridge.domain.model.AccountInfo
import com.stone.fridge.domain.model.Address
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
}
