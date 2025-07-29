package com.stone.fridge.di

import com.stone.fridge.data.repository.ChatRepositoryImpl
import com.stone.fridge.data.repository.FCMRepositoryImpl
import com.stone.fridge.data.repository.FridgeRepositoryImpl
import com.stone.fridge.data.repository.HomeRepositoryImpl
import com.stone.fridge.data.repository.LocalUserRepositoryImpl
import com.stone.fridge.data.repository.LoginRepositoryImpl
import com.stone.fridge.data.repository.MyRepositoryImpl
import com.stone.fridge.data.repository.NotificationRepositoryImpl
import com.stone.fridge.data.repository.PostRepositoryImpl
import com.stone.fridge.data.repository.TokenRepositoryImpl
import com.stone.fridge.data.repository.WebSocketRepositoryImpl
import com.stone.fridge.domain.repository.ChatRepository
import com.stone.fridge.domain.repository.FCMRepository
import com.stone.fridge.domain.repository.FridgeRepository
import com.stone.fridge.domain.repository.HomeRepository
import com.stone.fridge.domain.repository.LocalUserRepository
import com.stone.fridge.domain.repository.LoginRepository
import com.stone.fridge.domain.repository.MyRepository
import com.stone.fridge.domain.repository.NotificationRepository
import com.stone.fridge.domain.repository.PostRepository
import com.stone.fridge.domain.repository.TokenRepository
import com.stone.fridge.domain.repository.WebSocketRepository
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