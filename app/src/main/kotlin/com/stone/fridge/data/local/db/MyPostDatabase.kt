package com.stone.fridge.data.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.stone.fridge.data.local.entity.MyPostEntity
import com.stone.fridge.data.local.remote.MyPostDao
import com.stone.fridge.data.util.Converters

@Database(
    entities = [MyPostEntity::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class MyPostDatabase: RoomDatabase() {
    abstract val dao: MyPostDao
}