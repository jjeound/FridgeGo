package com.stone.fridge.core.data.login

import androidx.annotation.WorkerThread
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import com.stone.fridge.core.auth.TokenDataSource
import com.stone.fridge.core.data.util.PrefKeys.DONG
import com.stone.fridge.core.data.util.PrefKeys.NICKNAME
import com.stone.fridge.core.data.util.PrefKeys.USER_ID
import com.stone.fridge.core.model.AccountInfo
import com.stone.fridge.core.model.AddressInfo
import com.stone.fridge.core.model.KakaoAccessTokenRequest
import com.stone.fridge.core.model.Location
import com.stone.fridge.core.model.toAccountInfo
import com.stone.fridge.core.model.toAddressInfo
import com.stone.fridge.core.network.AppDispatchers
import com.stone.fridge.core.network.Dispatcher
import com.stone.fridge.core.network.service.LoginClient
import com.stone.fridge.core.network.service.MapClient
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class LoginRepositoryImpl @Inject constructor(
    private val loginClient: LoginClient,
    private val mapClient: MapClient,
    private val tokenDataSource: TokenDataSource,
    private val dataStore: DataStore<Preferences>,
    @param:Dispatcher(AppDispatchers.IO) private val ioDispatcher: CoroutineDispatcher,
): LoginRepository {

    @WorkerThread
    override fun kakaoLogin(accessToken: String): Flow<AccountInfo> = flow {
        loginClient.kakaoLogin(KakaoAccessTokenRequest(accessToken)).result?.let {
            tokenDataSource.saveAccessToken(it.accessToken)
            tokenDataSource.saveRefreshToken(it.refreshToken)
            saveUserId(userId = it.id)
            emit(it.toAccountInfo())
        }
    }.flowOn(ioDispatcher)

    @WorkerThread
    override fun setNickname(nickname: String): Flow<String> = flow {
        loginClient.setNickname(nickname = nickname).result?.let {
            saveNickname(nickname)
            emit(it)
        }
    }.flowOn(ioDispatcher)

    private suspend fun saveNickname(nickname: String) {
        dataStore.edit { prefs ->
            prefs[NICKNAME] = nickname
        }
    }

    private suspend fun saveUserId(userId: Long) {
        dataStore.edit { prefs ->
            prefs[USER_ID] = userId
        }
    }

    @WorkerThread
    override fun getAddressByCoord(
        x: String,
        y: String
    ): Flow<AddressInfo> = flow {
        val response = mapClient.getRegion(x = x, y = y).documents
        if(response?.isNotEmpty() == true) {
            emit(response[0].address.toAddressInfo())
        }
    }.flowOn(ioDispatcher)

    @WorkerThread
    override fun setLocation(district: String, neighborhood: String): Flow<String> = flow {
        loginClient.setLocation(Location(district, neighborhood)).result?.let {
            saveLocation(neighborhood)
            emit(it)
        }
    }.flowOn(ioDispatcher)

    private suspend fun saveLocation(neighborhood: String) {
        dataStore.edit { prefs ->
            prefs[DONG] = neighborhood
        }
    }
}