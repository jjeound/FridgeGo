package com.example.untitled_capstone.di

import android.app.Application
import android.content.Context
import com.example.untitled_capstone.data.local.db.FridgeItemDatabase
import com.example.untitled_capstone.data.local.db.MessageItemDatabase
import com.example.untitled_capstone.data.local.db.PostItemDatabase
import com.example.untitled_capstone.data.local.db.RecipeItemDatabase
import com.example.untitled_capstone.data.remote.manager.WebSocketManager
import com.example.untitled_capstone.data.remote.service.ChatApi
import com.example.untitled_capstone.data.remote.service.FridgeApi
import com.example.untitled_capstone.data.remote.service.HomeApi
import com.example.untitled_capstone.data.remote.service.LoginApi
import com.example.untitled_capstone.data.remote.service.MapApi
import com.example.untitled_capstone.data.remote.service.MyApi
import com.example.untitled_capstone.data.remote.service.PostApi
import com.example.untitled_capstone.data.repository.ChatRepositoryImpl
import com.example.untitled_capstone.data.repository.FridgeRepositoryImpl
import com.example.untitled_capstone.data.repository.HomeRepositoryImpl
import com.example.untitled_capstone.data.repository.LocalUserRepositoryImpl
import com.example.untitled_capstone.data.repository.LoginRepositoryImpl
import com.example.untitled_capstone.data.repository.MyRepositoryImpl
import com.example.untitled_capstone.data.repository.PostRepositoryImpl
import com.example.untitled_capstone.data.repository.WebSocketRepositoryImpl
import com.example.untitled_capstone.domain.repository.ChatRepository
import com.example.untitled_capstone.domain.repository.FridgeRepository
import com.example.untitled_capstone.domain.repository.HomeRepository
import com.example.untitled_capstone.domain.repository.LocalUserRepository
import com.example.untitled_capstone.domain.repository.LoginRepository
import com.example.untitled_capstone.domain.repository.MyRepository
import com.example.untitled_capstone.domain.repository.PostRepository
import com.example.untitled_capstone.domain.repository.TokenRepository
import com.example.untitled_capstone.domain.repository.WebSocketRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun provideHomeRepository(db: RecipeItemDatabase, api: HomeApi, @ApplicationContext context: Context): HomeRepository{
        return HomeRepositoryImpl(api, db, context)
    }

    @Provides
    @Singleton
    fun providePostRepository(db: PostItemDatabase,api: PostApi, @ApplicationContext context: Context): PostRepository{
        return PostRepositoryImpl(api, db, context)
    }

    @Provides
    @Singleton
    fun provideFridgeRepository(db: FridgeItemDatabase, api: FridgeApi): FridgeRepository{
        return FridgeRepositoryImpl(api, db)
    }

    @Provides
    @Singleton
    fun provideLoginRepository(api: LoginApi, mapApi: MapApi, tokenRepository: TokenRepository, @ApplicationContext context: Context): LoginRepository{
        return LoginRepositoryImpl(api, mapApi, tokenRepository, context)
    }

    @Provides
    @Singleton
    fun provideMyRepository(api: MyApi, tokenRepository: TokenRepository, @ApplicationContext context: Context): MyRepository{
        return MyRepositoryImpl(api, tokenRepository, context)
    }

    @Provides
    @Singleton
    fun provideLocalUserRepository(application: Application): LocalUserRepository{
        return LocalUserRepositoryImpl(application)
    }

    @Provides
    @Singleton
    fun provideChatRepository(api: ChatApi, db: MessageItemDatabase): ChatRepository{
        return ChatRepositoryImpl(api, db)
    }

    @Provides
    @Singleton
    fun provideWebSocketRepository(
        webSocketManager: WebSocketManager,
        db: MessageItemDatabase
    ): WebSocketRepository = WebSocketRepositoryImpl(webSocketManager, db)
}