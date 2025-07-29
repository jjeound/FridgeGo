package com.stone.fridge.data.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.stone.fridge.data.local.entity.LikedPostEntity
import com.stone.fridge.data.local.remote.LikedPostDao
import com.stone.fridge.data.util.Converters

@Database(
    entities = [LikedPostEntity::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class LikedPostDatabase: RoomDatabase() {
    abstract val dao: LikedPostDao
}