package com.example.untitled_capstone.data.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.untitled_capstone.data.local.entity.ProfileEntity
import com.example.untitled_capstone.data.local.remote.ProfileDao

@Database(
    entities = [ProfileEntity::class],
    version = 1,
    exportSchema = false
)
abstract class ProfileDatabase: RoomDatabase() {
    abstract val dao: ProfileDao
}