package com.stone.fridge.data.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.stone.fridge.data.local.entity.MessageItemEntity
import com.stone.fridge.data.local.remote.MessageDao

@Database(
    entities = [MessageItemEntity::class],
    version = 1,
    exportSchema = false
)
abstract class MessageItemDatabase: RoomDatabase() {
    abstract val dao: MessageDao
}