package com.stone.fridge.data.repository

import android.content.Context
import android.util.Log
import androidx.annotation.WorkerThread
import androidx.datastore.preferences.core.edit
import com.stone.fridge.core.util.PrefKeys.FCM_NEW_TOKEN
import com.stone.fridge.core.util.PrefKeys.FCM_OLD_TOKEN
import com.stone.fridge.data.remote.service.FcmApi
import com.stone.fridge.domain.repository.FCMRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import okio.IOException
import retrofit2.HttpException
import javax.inject.Inject

class FCMRepositoryImpl @Inject constructor(
    private val fcmApi: FcmApi,
    @ApplicationContext private val context: Context
): FCMRepository {
    val dataStore = context.dataStore
    @WorkerThread
    override suspend fun saveFcmToken(){
        val oldToken = dataStore.data.map { prefs ->
            prefs[FCM_OLD_TOKEN]
        }.first()
        val newToken = dataStore.data.map { prefs ->
            prefs[FCM_NEW_TOKEN]
        }.first()
        try {
            if(newToken == null && oldToken != null){
                Log.d("FCMRepositoryImpl", "Old token saved: $oldToken")
                fcmApi.saveFcmToken(oldToken)
            } else if (newToken != null && newToken != oldToken){
                Log.d("FCMRepositoryImpl", "New token saved: $newToken")
                fcmApi.saveFcmToken(newToken)
                saveToken(newToken)
            }
        } catch (e: IOException) {
            Log.d("FCM", "IOException: ${e.message}")
        } catch (e: HttpException) {
            Log.d("FCM", "HttpException: ${e.message}")
        }
    }

    private suspend fun saveToken(newToken: String){
        dataStore.edit { prefs ->
            prefs[FCM_OLD_TOKEN] = newToken
        }
    }

}