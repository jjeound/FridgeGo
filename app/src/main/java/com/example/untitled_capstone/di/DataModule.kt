package com.example.untitled_capstone.di

import com.example.untitled_capstone.data.repository.ChatRepositoryImpl
import com.example.untitled_capstone.data.repository.FCMRepositoryImpl
import com.example.untitled_capstone.data.repository.FridgeRepositoryImpl
import com.example.untitled_capstone.data.repository.HomeRepositoryImpl
import com.example.untitled_capstone.data.repository.LocalUserRepositoryImpl
import com.example.untitled_capstone.data.repository.LoginRepositoryImpl
import com.example.untitled_capstone.data.repository.MyRepositoryImpl
import com.example.untitled_capstone.data.repository.NotificationRepositoryImpl
import com.example.untitled_capstone.data.repository.PostRepositoryImpl
import com.example.untitled_capstone.data.repository.TokenRepositoryImpl
import com.example.untitled_capstone.data.repository.WebSocketRepositoryImpl
import com.example.untitled_capstone.domain.repository.ChatRepository
import com.example.untitled_capstone.domain.repository.FCMRepository
import com.example.untitled_capstone.domain.repository.FridgeRepository
import com.example.untitled_capstone.domain.repository.HomeRepository
import com.example.untitled_capstone.domain.repository.LocalUserRepository
import com.example.untitled_capstone.domain.repository.LoginRepository
import com.example.untitled_capstone.domain.repository.MyRepository
import com.example.untitled_capstone.domain.repository.NotificationRepository
import com.example.untitled_capstone.domain.repository.PostRepository
import com.example.untitled_capstone.domain.repository.TokenRepository
import com.example.untitled_capstone.domain.repository.WebSocketRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface DataModule {
    @Binds
    fun bindsHomeRepository(homeRepositoryImpl: HomeRepositoryImpl): HomeRepository

    @Binds
    fun bindsPostRepository(postRepositoryImpl: PostRepositoryImpl): PostRepository

    @Binds
    fun bindsFridgeRepository(fridgeRepositoryImpl: FridgeRepositoryImpl): FridgeRepository

    @Binds
    fun bindsChatRepository(chatRepositoryImpl: ChatRepositoryImpl): ChatRepository

    @Binds
    fun bindsWebSocketRepository(webSocketRepositoryImpl: WebSocketRepositoryImpl): WebSocketRepository

    @Binds
    fun bindsMyRepository(myRepositoryImpl: MyRepositoryImpl): MyRepository

    @Binds
    fun bindsLoginRepository(loginRepositoryImpl: LoginRepositoryImpl): LoginRepository

    @Binds
    fun bindsLocalUserRepository(localUserRepositoryImpl: LocalUserRepositoryImpl): LocalUserRepository

    @Binds
    fun bindsTokenRepository(tokenRepositoryImpl: TokenRepositoryImpl): TokenRepository

    @Binds
    fun bindsFCMRepository(fcmRepositoryImpl: FCMRepositoryImpl): FCMRepository

    @Binds
    fun bindsNotificationRepository(notificationRepositoryImpl: NotificationRepositoryImpl): NotificationRepository
}