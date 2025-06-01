package com.example.untitled_capstone.data.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.untitled_capstone.data.local.entity.LikedPostEntity
import com.example.untitled_capstone.data.local.remote.LikedPostDao
import com.example.untitled_capstone.data.util.Converters

@Database(
    entities = [LikedPostEntity::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class LikedPostDatabase: RoomDatabase() {
    abstract val dao: LikedPostDao
}