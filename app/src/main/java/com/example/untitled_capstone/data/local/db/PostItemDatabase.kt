package com.example.untitled_capstone.data.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.untitled_capstone.data.local.entity.PostItemEntity
import com.example.untitled_capstone.data.local.entity.RecipeItemEntity
import com.example.untitled_capstone.data.local.remote.PostItemDao
import com.example.untitled_capstone.data.local.remote.RecipeItemDao

@Database(
    entities = [PostItemEntity::class],
    version = 1,
    exportSchema = false
)
abstract class PostItemDatabase: RoomDatabase() {
    abstract val dao: PostItemDao
}