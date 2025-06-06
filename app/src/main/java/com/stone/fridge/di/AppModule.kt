package com.stone.fridge.di

import android.content.Context
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.room.Room
import com.stone.fridge.core.util.Constants.BASE_URL
import com.stone.fridge.core.util.Constants.KAKAO_BASE_URL
import com.stone.fridge.data.local.db.FridgeItemDatabase
import com.stone.fridge.data.local.db.LikedPostDatabase
import com.stone.fridge.data.local.db.MessageItemDatabase
import com.stone.fridge.data.local.db.MyPostDatabase
import com.stone.fridge.data.local.db.PostItemDatabase
import com.stone.fridge.data.local.db.RecipeItemDatabase
import com.stone.fridge.data.local.entity.FridgeItemEntity
import com.stone.fridge.data.local.entity.LikedPostEntity
import com.stone.fridge.data.local.entity.MessageItemEntity
import com.stone.fridge.data.local.entity.MyPostEntity
import com.stone.fridge.data.local.entity.PostItemEntity
import com.stone.fridge.data.local.entity.RecipeItemEntity
import com.stone.fridge.data.local.remote.FridgeItemDao
import com.stone.fridge.data.local.remote.LikedPostDao
import com.stone.fridge.data.local.remote.MessageDao
import com.stone.fridge.data.local.remote.MyPostDao
import com.stone.fridge.data.local.remote.PostItemDao
import com.stone.fridge.data.local.remote.RecipeItemDao
import com.stone.fridge.data.pagination.FridgePagingSource
import com.stone.fridge.data.pagination.LikedPostPagingSource
import com.stone.fridge.data.pagination.MessagePagingSource
import com.stone.fridge.data.pagination.MyPostPagingSource
import com.stone.fridge.data.pagination.PostPagingSource
import com.stone.fridge.data.pagination.RecipePagingSource
import com.stone.fridge.data.remote.manager.WebSocketManager
import com.stone.fridge.data.remote.service.ChatApi
import com.stone.fridge.data.remote.service.FcmApi
import com.stone.fridge.data.remote.service.FridgeApi
import com.stone.fridge.data.remote.service.HomeApi
import com.stone.fridge.data.remote.service.LoginApi
import com.stone.fridge.data.remote.service.MapApi
import com.stone.fridge.data.remote.service.MyApi
import com.stone.fridge.data.remote.service.PostApi
import com.stone.fridge.data.remote.service.TokenApi
import com.stone.fridge.data.util.ApiResponseAdapterFactory
import com.stone.fridge.data.util.FridgeFetchType
import com.stone.fridge.domain.repository.TokenRepository
import com.stone.fridge.domain.use_case.token.AuthAuthenticator
import com.stone.fridge.domain.use_case.token.AuthInterceptor
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.stone.fridge.data.remote.service.NotificationApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideRecipeItemDatabase(@ApplicationContext context: Context): RecipeItemDatabase{
        return Room.databaseBuilder(
            context, RecipeItemDatabase::class.java, "recipe_item_database"
        ).build()
    }

    @Provides
    @Singleton
    fun provideRecipeDao(db: RecipeItemDatabase): RecipeItemDao = db.dao

    @Provides
    @Singleton
    fun provideFridgeItemDatabase(@ApplicationContext context: Context): FridgeItemDatabase{
        return Room.databaseBuilder(
            context, FridgeItemDatabase::class.java, "fridge_item_database"
        ).build()
    }

    @Provides
    @Singleton
    fun provideFridgeDao(db: FridgeItemDatabase): FridgeItemDao = db.dao

    @Provides
    @Singleton
    fun providePostItemDatabase(@ApplicationContext context: Context): PostItemDatabase{
        return Room.databaseBuilder(
            context, PostItemDatabase::class.java, "post_item_database"
        ).build()
    }

    @Provides
    @Singleton
    fun providePostDao(db: PostItemDatabase): PostItemDao = db.dao

    @Provides
    @Singleton
    fun provideLikedPostItemDatabase(@ApplicationContext context: Context): LikedPostDatabase {
        return Room.databaseBuilder(
            context, LikedPostDatabase::class.java, "liked_post__database"
        ).build()
    }

    @Provides
    @Singleton
    fun provideLikedPostDao(db: LikedPostDatabase): LikedPostDao = db.dao

    @Provides
    @Singleton
    fun provideMyPostItemDatabase(@ApplicationContext context: Context): MyPostDatabase {
        return Room.databaseBuilder(
            context, MyPostDatabase::class.java, "my_post_database"
        ).build()
    }

    @Provides
    @Singleton
    fun provideMyPostDao(db: MyPostDatabase): MyPostDao = db.dao

    @Provides
    @Singleton
    fun provideMessageItemDatabase(@ApplicationContext context: Context): MessageItemDatabase{
        return Room.databaseBuilder(
            context, MessageItemDatabase::class.java, "message_item_database"
        ).build()
    }

    @Provides
    @Singleton
    fun provideMessageDao(db: MessageItemDatabase): MessageDao = db.dao

    @Provides
    @Singleton
    fun provideOkHttpClient(tokenRepository: TokenRepository): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(AuthInterceptor(tokenRepository))
            .authenticator(AuthAuthenticator(tokenRepository))
            .build()
    }

    @Provides
    @Singleton
    fun provideFridgeApi(gson: Gson, okHttpClient: OkHttpClient): FridgeApi {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
            .create(FridgeApi::class.java)
    }

    @Provides
    @Singleton
    fun provideLoginApi(gson: Gson): LoginApi {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
            .create(LoginApi::class.java)
    }

    @Provides
    @Singleton
    fun provideMyApi(gson: Gson, okHttpClient: OkHttpClient): MyApi {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
            .create(MyApi::class.java)
    }

    @Provides
    @Singleton
    fun provideHomeApi(gson: Gson, okHttpClient: OkHttpClient): HomeApi {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
            .create(HomeApi::class.java)
    }

    @Provides
    @Singleton
    fun providePostApi(gson: Gson, okHttpClient: OkHttpClient): PostApi {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
            .create(PostApi::class.java)
    }

    @Provides
    @Singleton
    fun provideMapApi(): MapApi {
        return Retrofit.Builder()
            .baseUrl(KAKAO_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(MapApi::class.java)
    }

    @Provides
    @Singleton
    fun provideTokenApi(gson: Gson): TokenApi {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
            .create(TokenApi::class.java)
    }

    @Provides
    @Singleton
    fun provideChatApi(gson: Gson, okHttpClient: OkHttpClient): ChatApi {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
            .create(ChatApi::class.java)
    }

    @Provides
    @Singleton
    fun provideFcmApi(gson: Gson, okHttpClient: OkHttpClient): FcmApi {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
            .create(FcmApi::class.java)
    }

    @Provides
    @Singleton
    fun provideNotificationApi(gson: Gson, okHttpClient: OkHttpClient): NotificationApi {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
            .create(NotificationApi::class.java)
    }

    @Provides
    @Singleton
    fun provideGson(): Gson {
        return GsonBuilder()
            .registerTypeAdapterFactory(ApiResponseAdapterFactory())
            .create()
    }

    @OptIn(ExperimentalPagingApi::class)
    @Provides
    @Singleton
    fun provideFridgePager(db: FridgeItemDatabase, api: FridgeApi, fetchType: FridgeFetchType): Pager<Int, FridgeItemEntity> {
        return Pager(
            config = PagingConfig(pageSize = 10),
            remoteMediator = FridgePagingSource(
                db = db,
                api = api,
                fetchType = fetchType
            ),
            pagingSourceFactory = {
                db.dao.getFridgeItems()
            }
        )
    }

    @OptIn(ExperimentalPagingApi::class)
    @Provides
    @Singleton
    fun provideRecipePager(db: RecipeItemDatabase, api: HomeApi): Pager<Int, RecipeItemEntity> {
        return Pager(
            config = PagingConfig(pageSize = 10),
            remoteMediator = RecipePagingSource(
                db = db,
                api = api
            ),
            pagingSourceFactory = {
                db.dao.getRecipeItems()
            }
        )
    }

    @OptIn(ExperimentalPagingApi::class)
    @Provides
    @Singleton
    fun providePostPager(db: PostItemDatabase, api: PostApi): Pager<Int, PostItemEntity> {
        return Pager(
            config = PagingConfig(pageSize = 10),
            remoteMediator = PostPagingSource(
                db = db,
                api = api
            ),
            pagingSourceFactory = {
                db.dao.getPostItems()
            }
        )
    }

    @OptIn(ExperimentalPagingApi::class)
    @Provides
    @Singleton
    fun provideLikedPostPager(db: LikedPostDatabase, api: PostApi): Pager<Int, LikedPostEntity> {
        return Pager(
            config = PagingConfig(pageSize = 10),
            remoteMediator = LikedPostPagingSource(
                db = db,
                api = api
            ),
            pagingSourceFactory = {
                db.dao.getPostItems()
            }
        )
    }

    @OptIn(ExperimentalPagingApi::class)
    @Provides
    @Singleton
    fun provideMyPostPager(db: MyPostDatabase, api: PostApi): Pager<Int, MyPostEntity> {
        return Pager(
            config = PagingConfig(pageSize = 10),
            remoteMediator = MyPostPagingSource(
                db = db,
                api = api
            ),
            pagingSourceFactory = {
                db.dao.getPostItems()
            }
        )
    }

    @OptIn(ExperimentalPagingApi::class)
    @Provides
    @Singleton
    fun provideMessagePager(roomId: Long, db: MessageItemDatabase, api: ChatApi): Pager<Int, MessageItemEntity> {
        return Pager(
            config = PagingConfig(pageSize = 20),
            remoteMediator = MessagePagingSource(
                roomId = roomId,
                db = db,
                api = api,
            ),
            pagingSourceFactory = {
                db.dao.getMessagesPaging(roomId)
            }
        )
    }

    @Provides
    @Singleton
    fun provideWebSocketManager(): WebSocketManager = WebSocketManager()
}