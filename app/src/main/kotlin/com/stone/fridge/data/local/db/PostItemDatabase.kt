package com.stone.fridge.data.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.stone.fridge.data.local.entity.PostItemEntity
import com.stone.fridge.data.local.remote.PostItemDao
import com.stone.fridge.data.util.Converters

@Database(
    entities = [PostItemEntity::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class PostItemDatabase: RoomDatabase() {
    abstract val dao: PostItemDao
}