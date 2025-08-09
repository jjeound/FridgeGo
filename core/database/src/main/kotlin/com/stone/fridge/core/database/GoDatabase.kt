package com.stone.fridge.core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.stone.fridge.core.database.dao.FridgeItemDao
import com.stone.fridge.core.database.dao.LikedPostDao
import com.stone.fridge.core.database.dao.MessageDao
import com.stone.fridge.core.database.dao.MyPostDao
import com.stone.fridge.core.database.dao.PostItemDao
import com.stone.fridge.core.database.dao.RecipeItemDao
import com.stone.fridge.core.database.model.FridgeItemEntity
import com.stone.fridge.core.database.model.LikedPostEntity
import com.stone.fridge.core.database.model.MessageItemEntity
import com.stone.fridge.core.database.model.MyPostEntity
import com.stone.fridge.core.database.model.PostItemEntity
import com.stone.fridge.core.database.model.RecipeItemEntity
import com.stone.fridge.core.database.util.Converters

@Database(
    entities = [FridgeItemEntity::class, LikedPostEntity::class, MessageItemEntity::class,
        MyPostEntity::class, PostItemEntity::class, RecipeItemEntity::class],
    version = 1,
    exportSchema = true
)
@TypeConverters(
    Converters::class
)
abstract class GoDatabase: RoomDatabase(){
    abstract fun fridgeItemDao(): FridgeItemDao
    abstract fun likedPostDao(): LikedPostDao
    abstract fun messageItemDao(): MessageDao
    abstract fun myPostDao(): MyPostDao
    abstract fun postItemDao(): PostItemDao
    abstract fun recipeItemDao(): RecipeItemDao
}