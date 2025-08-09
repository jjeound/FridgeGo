package com.stone.fridge.core.paging.di

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.stone.fridge.core.database.GoDatabase
import com.stone.fridge.core.database.dao.LikedPostDao
import com.stone.fridge.core.database.dao.MessageDao
import com.stone.fridge.core.database.dao.MyPostDao
import com.stone.fridge.core.database.dao.PostItemDao
import com.stone.fridge.core.database.dao.RecipeItemDao
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
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal object PagingModule {

    @OptIn(ExperimentalPagingApi::class)
    @Provides
    @Singleton
    fun provideRecipePager(db: GoDatabase, dao: RecipeItemDao, homeClient: HomeClient): Pager<Int, RecipeItemEntity> {
        return Pager(
            config = PagingConfig(pageSize = 10),
            remoteMediator = RecipePagingSource(
                db = db,
                homeClient = homeClient
            ),
            pagingSourceFactory = {
                dao.getRecipeItems()
            }
        )
    }

    @OptIn(ExperimentalPagingApi::class)
    @Provides
    @Singleton
    fun providePostPager(db: GoDatabase, dao: PostItemDao, postClient: PostClient): Pager<Int, PostItemEntity> {
        return Pager(
            config = PagingConfig(pageSize = 10),
            remoteMediator = PostPagingSource(
                db = db,
                postClient = postClient
            ),
            pagingSourceFactory = {
                dao.getPostItems()
            }
        )
    }

    @OptIn(ExperimentalPagingApi::class)
    @Provides
    @Singleton
    fun provideLikedPostPager(db: GoDatabase, dao: LikedPostDao, postClient: PostClient): Pager<Int, LikedPostEntity> {
        return Pager(
            config = PagingConfig(pageSize = 10),
            remoteMediator = LikedPostPagingSource(
                db = db,
                postClient = postClient
            ),
            pagingSourceFactory = {
                dao.getPostItems()
            }
        )
    }

    @OptIn(ExperimentalPagingApi::class)
    @Provides
    @Singleton
    fun provideMyPostPager(db: GoDatabase, dao: MyPostDao , postClient: PostClient): Pager<Int, MyPostEntity> {
        return Pager(
            config = PagingConfig(pageSize = 10),
            remoteMediator = MyPostPagingSource(
                db = db,
                postClient = postClient
            ),
            pagingSourceFactory = {
                dao.getPostItems()
            }
        )
    }

//    @OptIn(ExperimentalPagingApi::class)
//    @Provides
//    @Singleton
//    fun provideMessagePager(roomId: Long, db: GoDatabase, dao: MessageDao, chatClient: ChatClient): Pager<Int, MessageItemEntity> {
//        return Pager(
//            config = PagingConfig(pageSize = 20),
//            remoteMediator = MessagePagingSource(
//                roomId = roomId,
//                db = db,
//                chatClient = chatClient,
//            ),
//            pagingSourceFactory = {
//                dao.getMessagesPaging(roomId)
//            }
//        )
//    }
}