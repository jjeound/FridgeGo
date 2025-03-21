package com.example.untitled_capstone.domain.repository

import kotlinx.coroutines.flow.Flow

interface LocalUserManger {
    suspend fun saveAppEntry()
    fun readAppEntry(): Flow<Boolean>
}