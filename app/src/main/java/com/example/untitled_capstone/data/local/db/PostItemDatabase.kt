package com.example.untitled_capstone.data.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.untitled_capstone.data.local.entity.PostItemEntity
import com.example.untitled_capstone.data.local.remote.PostItemDao
import com.example.untitled_capstone.data.util.Converters

@Database(
    entities = [PostItemEntity::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class PostItemDatabase: RoomDatabase() {
    abstract val dao: PostItemDao
}