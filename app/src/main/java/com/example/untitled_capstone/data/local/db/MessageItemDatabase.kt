package com.example.untitled_capstone.data.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.untitled_capstone.data.local.entity.MessageItemEntity
import com.example.untitled_capstone.data.local.remote.MessageDao

@Database(
    entities = [MessageItemEntity::class],
    version = 1,
    exportSchema = false
)
abstract class MessageItemDatabase: RoomDatabase() {
    abstract val dao: MessageDao
}