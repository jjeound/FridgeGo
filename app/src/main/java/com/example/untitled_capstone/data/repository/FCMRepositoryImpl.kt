package com.example.untitled_capstone.data.repository

import android.util.Log
import androidx.annotation.WorkerThread
import com.example.untitled_capstone.data.remote.service.FcmApi
import com.example.untitled_capstone.domain.repository.FCMRepository
import okio.IOException
import retrofit2.HttpException
import javax.inject.Inject

class FCMRepositoryImpl @Inject constructor(
    private val fcmApi: FcmApi,
): FCMRepository {
    @WorkerThread
    override suspend fun saveFcmToken(token: String){
        try {
            val response = fcmApi.saveFcmToken(token)
            if(response.isSuccess){
                Log.d("FCM", "token saved")
            } else {
                Log.d("FCM", "Error saving token: ${response.message}")
            }
        } catch (e: IOException) {
            Log.d("FCM", "IOException: ${e.message}")
        } catch (e: HttpException) {
            Log.d("FCM", "HttpException: ${e.message}")
        }
    }

}