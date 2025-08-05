package com.stone.fridge.core.paging.di

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.stone.fridge.core.database.GoDatabase
import com.stone.fridge.core.database.model.LikedPostEntity
import com.stone.fridge.core.database.model.MessageItemEntity
import com.stone.fridge.core.database.model.MyPostEntity
import com.stone.fridge.core.database.model.PostItemEntity
import com.stone.fridge.core.database.model.RecipeItemEntity
import com.stone.fridge.core.network.service.ChatClient
import com.stone.fridge.core.network.service.HomeClient
import com.stone.fridge.core.network.service.PostClient
import com.stone.fridge.core.paging.LikedPostPagingSource
import com.stone.fridge.core.paging.MessagePagingSource
import com.stone.fridge.core.paging.MyPostPagingSource
import com.stone.fridge.core.paging.PostPagingSource
import com.stone.fridge.core.paging.RecipePagingSource
import dagger.Provides
import javax.inject.Singleton

internal object PagingModule {

    @OptIn(ExperimentalPagingApi::class)
    @Provides
    @Singleton
    fun provideRecipePager(db: GoDatabase, homeClient: HomeClient): Pager<Int, RecipeItemEntity> {
        return Pager(
            config = PagingConfig(pageSize = 10),
            remoteMediator = RecipePagingSource(
                db = db,
                homeClient = homeClient
            ),
            pagingSourceFactory = {
                db.recipeItemDao().getRecipeItems()
            }
        )
    }

    @OptIn(ExperimentalPagingApi::class)
    @Provides
    @Singleton
    fun providePostPager(db: GoDatabase, postClient: PostClient): Pager<Int, PostItemEntity> {
        return Pager(
            config = PagingConfig(pageSize = 10),
            remoteMediator = PostPagingSource(
                db = db,
                postClient = postClient
            ),
            pagingSourceFactory = {
                db.postItemDao().getPostItems()
            }
        )
    }

    @OptIn(ExperimentalPagingApi::class)
    @Provides
    @Singleton
    fun provideLikedPostPager(db: GoDatabase, postClient: PostClient): Pager<Int, LikedPostEntity> {
        return Pager(
            config = PagingConfig(pageSize = 10),
            remoteMediator = LikedPostPagingSource(
                db = db,
                postClient = postClient
            ),
            pagingSourceFactory = {
                db.likedPostDao().getPostItems()
            }
        )
    }

    @OptIn(ExperimentalPagingApi::class)
    @Provides
    @Singleton
    fun provideMyPostPager(db: GoDatabase, postClient: PostClient): Pager<Int, MyPostEntity> {
        return Pager(
            config = PagingConfig(pageSize = 10),
            remoteMediator = MyPostPagingSource(
                db = db,
                postClient = postClient
            ),
            pagingSourceFactory = {
                db.myPostDao().getPostItems()
            }
        )
    }

    @OptIn(ExperimentalPagingApi::class)
    @Provides
    @Singleton
    fun provideMessagePager(roomId: Long, db: GoDatabase, chatClient: ChatClient): Pager<Int, MessageItemEntity> {
        return Pager(
            config = PagingConfig(pageSize = 20),
            remoteMediator = MessagePagingSource(
                roomId = roomId,
                db = db,
                chatClient = chatClient,
            ),
            pagingSourceFactory = {
                db.messageItemDao().getMessagesPaging(roomId)
            }
        )
    }
}