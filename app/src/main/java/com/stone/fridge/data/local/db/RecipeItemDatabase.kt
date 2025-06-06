package com.stone.fridge.data.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.stone.fridge.data.local.entity.RecipeItemEntity
import com.stone.fridge.data.local.remote.RecipeItemDao

@Database(
    entities = [RecipeItemEntity::class],
    version = 1,
    exportSchema = false
)
abstract class RecipeItemDatabase: RoomDatabase() {
    abstract val dao: RecipeItemDao
}