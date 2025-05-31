package com.example.untitled_capstone.domain.repository

import androidx.annotation.WorkerThread

interface FCMRepository {
    @WorkerThread
    suspend fun saveFcmToken(token: String)
}