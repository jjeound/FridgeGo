package com.stone.fridge.core.auth

import android.util.Base64
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject

val ACCESS_TOKEN_KEY = stringPreferencesKey("access_token")
val REFRESH_TOKEN_KEY = stringPreferencesKey("refresh_token")

class TokenDataSourceImpl @Inject constructor(
    private val dataStore: DataStore<Preferences>
): TokenDataSource {

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

//    override suspend fun refreshToken(refreshToken: String): Token? {
//        return try {
//            val response = tokenClient.refreshToken(refreshToken)
//            if(response.isSuccess){
//                Log.d("TokenRepositoryImpl", "토큰 갱신 성공")
//                response.result
//            } else {
//                Log.d("TokenRepositoryImpl", "토큰 갱신 실패: ${response.message}")
//                null
//            }
//        } catch (e: IOException) {
//            throw e
//        } catch (e: HttpException) {
//            if (e.code() == 401) {
//                Log.d("TokenRepositoryImpl", "토큰 갱신 실패: ${e.toString()}")
//                deleteTokens() // 토큰이 만료되었으므로 삭제
//            }
//            throw e
//        }
//    }
//
//    override suspend fun refreshAndSaveToken(): Token? {
//        val mutex = Mutex()
//        val refreshToken = getRefreshToken() ?: return null
//        mutex.withLock {
//            val token = refreshToken(refreshToken)
//            token?.let {
//                saveAccessToken(it.accessToken)
//                saveRefreshToken(it.refreshToken)
//                return it
//            }
//        }
//        return null
//    }
}

