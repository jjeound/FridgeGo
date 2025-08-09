package com.stone.fridge.core.data.login

import androidx.annotation.WorkerThread
import com.stone.fridge.core.data.util.Resource
import com.stone.fridge.core.model.AccountInfo
import com.stone.fridge.core.model.AddressInfo
import kotlinx.coroutines.flow.Flow

interface LoginRepository {
    @WorkerThread
    fun kakaoLogin(accessToken: String): Flow<AccountInfo>
    @WorkerThread
    fun setNickname(nickname: String): Flow<String>
    @WorkerThread
    fun getAddressByCoord(x: String, y: String): Flow<AddressInfo>
    @WorkerThread
    fun setLocation(district: String, neighborhood: String): Flow<String>
}
