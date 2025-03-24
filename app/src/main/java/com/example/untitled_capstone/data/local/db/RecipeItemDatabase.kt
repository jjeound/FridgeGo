package com.example.untitled_capstone.data.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.untitled_capstone.data.local.entity.RecipeItemEntity
import com.example.untitled_capstone.data.local.remote.RecipeItemDao

@Database(
    entities = [RecipeItemEntity::class],
    version = 1,
    exportSchema = false
)
abstract class RecipeItemDatabase: RoomDatabase() {
    abstract val dao: RecipeItemDao
}