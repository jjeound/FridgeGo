package com.stone.fridge.core.database.di

import com.stone.fridge.core.database.GoDatabase
import com.stone.fridge.core.database.dao.FridgeItemDao
import com.stone.fridge.core.database.dao.LikedPostDao
import com.stone.fridge.core.database.dao.MessageDao
import com.stone.fridge.core.database.dao.MyPostDao
import com.stone.fridge.core.database.dao.PostItemDao
import com.stone.fridge.core.database.dao.RecipeItemDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal object DaoModule {
    @Provides
    @Singleton
    fun providesFridgeItemDao(
        database: GoDatabase,
    ): FridgeItemDao = database.fridgeItemDao()

    @Provides
    @Singleton
    fun providesLikedPostDao(
        database: GoDatabase,
    ): LikedPostDao = database.likedPostDao()

    @Provides
    @Singleton
    fun providesMessageItemDao(
        database: GoDatabase,
    ): MessageDao = database.messageItemDao()

    @Provides
    @Singleton
    fun providesMyPostDao(
        database: GoDatabase,
    ): MyPostDao = database.myPostDao()

    @Provides
    @Singleton
    fun providesPostItemDao(
        database: GoDatabase,
    ): PostItemDao = database.postItemDao()

    @Provides
    @Singleton
    fun providesRecipeItemDao(
        database: GoDatabase,
    ): RecipeItemDao = database.recipeItemDao()
}