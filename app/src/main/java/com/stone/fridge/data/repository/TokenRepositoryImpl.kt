package com.stone.fridge.data.repository

import android.content.Context
import android.util.Base64
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
import com.stone.fridge.data.util.ErrorCode.JWT4004
import com.stone.fridge.domain.repository.TokenRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import okio.IOException
import retrofit2.HttpException
import javax.inject.Inject

class TokenRepositoryImpl @Inject constructor(
    @ApplicationContext context: Context,
    private val api: TokenApi
): TokenRepository {

    val dataStore = context.dataStore


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
                Resource.Success(response)
            }else{
                Resource.Error(response.message)
            }
        } catch (e: IOException) {
            Resource.Error(e.toString())
        } catch (e: HttpException) {
            Resource.Error(e.toString())
        }
    }

    override suspend fun refreshAndSaveToken(): TokenDto? {
        val refreshToken = getRefreshToken() ?: return null
        val response = refreshToken(refreshToken)

        return when (response) {
            is Resource.Success -> {
                val body = response.data
                if (body == null || body.code == JWT4004 || body.result == null) {
                    deleteTokens()
                    null
                } else {
                    saveAccessToken(body.result.accessToken)
                    saveRefreshToken(body.result.refreshToken)
                    body.result
                }
            }
            is Resource.Error -> {
                deleteTokens()
                null
            }
            else -> null // Loading이 들어오면 무시
        }
    }
}

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "secure_prefs")