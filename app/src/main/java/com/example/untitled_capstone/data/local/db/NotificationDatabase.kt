package com.example.untitled_capstone.data.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.untitled_capstone.data.local.entity.NotificationEntity
import com.example.untitled_capstone.data.local.remote.NotificationDao


@Database(
    entities = [NotificationEntity::class],
    version = 1,
    exportSchema = false
)
abstract class NotificationDatabase : RoomDatabase() {
    abstract val dao: NotificationDao
}