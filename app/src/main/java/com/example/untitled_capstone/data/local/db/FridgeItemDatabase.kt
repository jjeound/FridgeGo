package com.example.untitled_capstone.data.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.untitled_capstone.data.local.remote.FridgeItemDao
import com.example.untitled_capstone.data.local.entity.FridgeItemEntity

@Database(
    entities = [FridgeItemEntity::class],
    version = 1,
    exportSchema = false
)
abstract class FridgeItemDatabase: RoomDatabase() {
    abstract val dao: FridgeItemDao
}