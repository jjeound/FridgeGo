package com.stone.fridge.data.repository

import android.content.Context
import android.util.Base64
import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import com.stone.fridge.core.util.Crypto
import com.stone.fridge.core.util.PrefKeys.ACCESS_TOKEN_KEY
import com.stone.fridge.core.util.PrefKeys.REFRESH_TOKEN_KEY
import com.stone.fridge.core.util.Resource
import com.stone.fridge.data.remote.dto.ApiResponse
import com.stone.fridge.data.remote.dto.TokenDto
import com.stone.fridge.data.remote.service.TokenApi
import com.stone.fridge.data.util.ErrorCode.UNAUTHORIZED
import com.stone.fridge.domain.repository.TokenRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import okio.IOException
import retrofit2.HttpException
import javax.inject.Inject

class TokenRepositoryImpl @Inject constructor(
    @ApplicationContext context: Context,
    private val api: TokenApi
): TokenRepository {

    val dataStore = context.dataStore
    private val mutex = Mutex()

    override suspend fun getAccessToken(): String? {
        val base64 = dataStore.data
            .map { prefs -> prefs[ACCESS_TOKEN_KEY] ?: return@map null }
            .first() ?: return null

        val encryptedBytes = Base64.decode(base64, Base64.DEFAULT)
        val decryptedBytes = Crypto.decrypt(encryptedBytes)
        return String(decryptedBytes)
    }

    override suspend fun getRefreshToken(): String? {
        val base64 = dataStore.data
            .map { prefs -> prefs[REFRESH_TOKEN_KEY] ?: return@map null }
            .first() ?: return null

        val encryptedBytes = Base64.decode(base64, Base64.DEFAULT)
        val decryptedBytes = Crypto.decrypt(encryptedBytes)
        return String(decryptedBytes)
    }

    override suspend fun saveAccessToken(accessToken: String) {
        val encryptedBytes = Crypto.encrypt(accessToken.toByteArray())
        val base64 = Base64.encodeToString(encryptedBytes, Base64.DEFAULT)

        dataStore.edit { prefs ->
            prefs[ACCESS_TOKEN_KEY] = base64
        }
    }

    override suspend fun saveRefreshToken(refreshToken: String) {
        val encryptedBytes = Crypto.encrypt(refreshToken.toByteArray())
        val base64 = Base64.encodeToString(encryptedBytes, Base64.DEFAULT)

        dataStore.edit { prefs ->
            prefs[REFRESH_TOKEN_KEY] = base64
        }
    }

    override suspend fun deleteTokens(){
        dataStore.edit { prefs ->
            prefs.remove(ACCESS_TOKEN_KEY)
            prefs.remove(REFRESH_TOKEN_KEY)
        }
    }

    override suspend fun refreshToken(refreshToken: String): Resource<ApiResponse<TokenDto>> {
        return try {
            val response = api.refreshToken(refreshToken)
            if(response.isSuccess){
                response.result?.let {
                    Log.d("TokenRepositoryImpl", "토큰 갱신 성공")
                    Resource.Success(response)
                } ?: run {
                    Resource.Error(response.message)
                }
            }else{
                Resource.Error(response.message)
            }
        } catch (e: IOException) {
            Resource.Error(e.toString())
        } catch (e: HttpException) {
            if (e.code().toString() == UNAUTHORIZED) {
                Log.d("TokenRepositoryImpl", "토큰 갱신 실패: ${e.toString()}")
                deleteTokens() // 토큰이 만료되었으므로 삭제
            }
            Resource.Error(e.toString())
        }
    }

    override suspend fun refreshAndSaveToken(): TokenDto? {
        return mutex.withLock {
            val refreshToken = getRefreshToken() ?: return null
            val response = refreshToken(refreshToken)

            when (response) {
                is Resource.Success -> {
                    val body = response.data!!
                    saveAccessToken(body.result!!.accessToken)
                    saveRefreshToken(body.result.refreshToken)
                    body.result
                }
                else -> null
            }
        }
    }
}

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "secure_prefs")