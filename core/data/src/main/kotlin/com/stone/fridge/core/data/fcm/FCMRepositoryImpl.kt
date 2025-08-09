package com.stone.fridge.core.data.fcm

import android.util.Log
import androidx.annotation.WorkerThread
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import com.stone.fridge.core.data.util.PrefKeys.FCM_NEW_TOKEN
import com.stone.fridge.core.data.util.PrefKeys.FCM_OLD_TOKEN
import com.stone.fridge.core.network.service.FcmClient
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import okio.IOException
import retrofit2.HttpException
import javax.inject.Inject


class FCMRepositoryImpl @Inject constructor(
    private val fcmClient: FcmClient,
    private val dataStore: DataStore<Preferences>
): FCMRepository {
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
                fcmClient.saveFcmToken(oldToken)
            } else if (newToken != null && newToken != oldToken){
                Log.d("FCMRepositoryImpl", "New token saved: $newToken")
                fcmClient.saveFcmToken(newToken)
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