package com.example.untitled_capstone.domain.repository


interface LocalUserRepository {
    suspend fun saveAppEntry()
    suspend fun readAppEntry(): Boolean
}