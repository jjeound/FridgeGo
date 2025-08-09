package com.stone.fridge.core.data.local

interface LocalUserRepository {
    suspend fun saveAppEntry()
    suspend fun readAppEntry(): Boolean
    suspend fun getNickname(): String?
    suspend fun getUserId(): Long?
    suspend fun getLocation(): String?
}