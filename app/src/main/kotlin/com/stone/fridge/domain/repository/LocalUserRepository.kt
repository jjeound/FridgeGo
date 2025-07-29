package com.stone.fridge.domain.repository


interface LocalUserRepository {
    suspend fun saveAppEntry()
    suspend fun readAppEntry(): Boolean
}