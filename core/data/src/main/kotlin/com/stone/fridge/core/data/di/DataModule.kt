package com.stone.fridge.core.data.di

import com.stone.fridge.core.data.chat.ChatRepository
import com.stone.fridge.core.data.chat.ChatRepositoryImpl
import com.stone.fridge.core.data.fcm.FCMRepository
import com.stone.fridge.core.data.fcm.FCMRepositoryImpl
import com.stone.fridge.core.data.fridge.FridgeRepository
import com.stone.fridge.core.data.fridge.FridgeRepositoryImpl
import com.stone.fridge.core.data.home.HomeRepository
import com.stone.fridge.core.data.home.HomeRepositoryImpl
import com.stone.fridge.core.data.local.LocalUserRepository
import com.stone.fridge.core.data.local.LocalUserRepositoryImpl
import com.stone.fridge.core.data.login.LoginRepository
import com.stone.fridge.core.data.login.LoginRepositoryImpl
import com.stone.fridge.core.data.my.MyRepository
import com.stone.fridge.core.data.my.MyRepositoryImpl
import com.stone.fridge.core.data.notification.NotificationRepository
import com.stone.fridge.core.data.notification.NotificationRepositoryImpl
import com.stone.fridge.core.data.post.PostRepository
import com.stone.fridge.core.data.post.PostRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
internal interface DataModule {
    @Binds
    fun bindsHomeRepository(homeRepositoryImpl: HomeRepositoryImpl): HomeRepository

    @Binds
    fun bindsPostRepository(postRepositoryImpl: PostRepositoryImpl): PostRepository

    @Binds
    fun bindsFridgeRepository(fridgeRepositoryImpl: FridgeRepositoryImpl): FridgeRepository

    @Binds
    fun bindsChatRepository(chatRepositoryImpl: ChatRepositoryImpl): ChatRepository

    @Binds
    fun bindsMyRepository(myRepositoryImpl: MyRepositoryImpl): MyRepository

    @Binds
    fun bindsLoginRepository(loginRepositoryImpl: LoginRepositoryImpl): LoginRepository

    @Binds
    fun bindsLocalUserRepository(localUserRepositoryImpl: LocalUserRepositoryImpl): LocalUserRepository

    @Binds
    fun bindsFCMRepository(fcmRepositoryImpl: FCMRepositoryImpl): FCMRepository

    @Binds
    fun bindsNotificationRepository(notificationRepositoryImpl: NotificationRepositoryImpl): NotificationRepository
}