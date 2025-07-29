package com.stone.fridge.data.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.stone.fridge.data.local.remote.FridgeItemDao
import com.stone.fridge.data.local.entity.FridgeItemEntity

@Database(
    entities = [FridgeItemEntity::class],
    version = 1,
    exportSchema = false
)
abstract class FridgeItemDatabase: RoomDatabase() {
    abstract val dao: FridgeItemDao
}