package com.stone.fridge.core.data.fcm

import androidx.annotation.WorkerThread

interface FCMRepository {
    @WorkerThread
    suspend fun saveFcmToken()
}