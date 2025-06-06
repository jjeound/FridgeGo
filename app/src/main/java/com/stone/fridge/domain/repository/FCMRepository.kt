package com.stone.fridge.domain.repository

import androidx.annotation.WorkerThread

interface FCMRepository {
    @WorkerThread
    suspend fun saveFcmToken(token: String)
}