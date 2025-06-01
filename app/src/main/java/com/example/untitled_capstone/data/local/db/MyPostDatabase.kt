package com.example.untitled_capstone.data.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.untitled_capstone.data.local.entity.MyPostEntity
import com.example.untitled_capstone.data.local.remote.MyPostDao
import com.example.untitled_capstone.data.util.Converters

@Database(
    entities = [MyPostEntity::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class MyPostDatabase: RoomDatabase() {
    abstract val dao: MyPostDao
}