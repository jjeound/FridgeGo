package com.stone.fridge.core.network.di

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.stone.fridge.core.network.ApiResponseAdapterFactory
import com.stone.fridge.core.network.AuthClient
import com.stone.fridge.core.network.BaseClient
import com.stone.fridge.core.network.BuildConfig
import com.stone.fridge.core.network.manager.AuthAuthenticator
import com.stone.fridge.core.network.manager.AuthInterceptor
import com.stone.fridge.core.network.manager.WebSocketManager
import com.stone.fridge.core.network.service.ChatApi
import com.stone.fridge.core.network.service.ChatClient
import com.stone.fridge.core.network.service.FcmApi
import com.stone.fridge.core.network.service.FcmClient
import com.stone.fridge.core.network.service.FridgeApi
import com.stone.fridge.core.network.service.FridgeClient
import com.stone.fridge.core.network.service.HomeApi
import com.stone.fridge.core.network.service.HomeClient
import com.stone.fridge.core.network.service.LoginApi
import com.stone.fridge.core.network.service.LoginClient
import com.stone.fridge.core.network.service.MapApi
import com.stone.fridge.core.network.service.MapClient
import com.stone.fridge.core.network.service.MyApi
import com.stone.fridge.core.network.service.MyClient
import com.stone.fridge.core.network.service.NotificationApi
import com.stone.fridge.core.network.service.NotificationClient
import com.stone.fridge.core.network.service.PostApi
import com.stone.fridge.core.network.service.PostClient
import com.stone.fridge.core.network.service.TokenApi
import com.stone.fridge.core.network.service.TokenClient
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal object NetworkModule {
    @BaseClient
    @Provides
    @Singleton
    fun provideOkHttpClient(
        authAuthenticator: AuthAuthenticator,
        authInterceptor: AuthInterceptor
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(authInterceptor)
            .authenticator(authAuthenticator)
            .apply {
                addInterceptor(HttpLoggingInterceptor()
                    .apply {
                        if (BuildConfig.DEBUG) {
                            setLevel(HttpLoggingInterceptor.Level.BODY)
                        }
                    }
                )
            }
            .build()
    }

    @AuthClient
    @Provides
    @Singleton
    fun provideAuthClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor().apply {
                if (BuildConfig.DEBUG) {
                    setLevel(HttpLoggingInterceptor.Level.BODY)
                }
            })
            .build()
    }

    @BaseClient
    @Provides
    @Singleton
    fun provideBaseRetrofit(@BaseClient okHttpClient: OkHttpClient, gson: Gson): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://api.refrigerator.asia/")
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
    }

    @AuthClient
    @Provides
    @Singleton
    fun provideAuthRetrofit(@AuthClient okHttpClient: OkHttpClient, gson: Gson): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://api.refrigerator.asia/")
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
    }

    @Provides
    @Singleton
    fun provideFridgeApi(@BaseClient retrofit: Retrofit): FridgeApi {
        return retrofit.create(FridgeApi::class.java)
    }

    @Provides
    @Singleton
    fun provideFridgeClient(fridgeApi: FridgeApi): FridgeClient {
        return FridgeClient(fridgeApi)
    }

    @Provides
    @Singleton
    fun provideLoginApi(@BaseClient retrofit: Retrofit): LoginApi {
        return retrofit.create(LoginApi::class.java)
    }

    @Provides
    @Singleton
    fun provideLoginClient(loginApi: LoginApi): LoginClient {
        return LoginClient(loginApi)
    }

    @Provides
    @Singleton
    fun provideMyApi(@BaseClient retrofit: Retrofit): MyApi {
        return retrofit.create(MyApi::class.java)
    }

    @Provides
    @Singleton
    fun provideMyApiClient(myApi: MyApi): MyClient {
        return MyClient(myApi)
    }

    @Provides
    @Singleton
    fun provideHomeApi(@BaseClient retrofit: Retrofit): HomeApi {
        return retrofit.create(HomeApi::class.java)
    }

    @Provides
    @Singleton
    fun provideHomeApiClient(homeApi: HomeApi): HomeClient {
        return HomeClient(homeApi)
    }

    @Provides
    @Singleton
    fun providePostApi(@BaseClient retrofit: Retrofit): PostApi {
        return retrofit.create(PostApi::class.java)
    }

    @Provides
    @Singleton
    fun providePostApiClient(postApi: PostApi): PostClient {
        return PostClient(postApi)
    }

    @Provides
    @Singleton
    fun provideMapApi(): MapApi {
        return Retrofit.Builder()
            .baseUrl("https://dapi.kakao.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(MapApi::class.java)
    }

    @Provides
    @Singleton
    fun provideMapApiClient(mapApi: MapApi): MapClient {
        return MapClient(mapApi)
    }

    @Provides
    @Singleton
    fun provideTokenApi(@AuthClient retrofit: Retrofit): TokenApi {
        return retrofit.create(TokenApi::class.java)
    }

    @Provides
    @Singleton
    fun provideTokenClient(tokenApi: TokenApi): TokenClient {
        return TokenClient(tokenApi)
    }

    @Provides
    @Singleton
    fun provideChatApi(@BaseClient retrofit: Retrofit): ChatApi {
        return retrofit.create(ChatApi::class.java)
    }

    @Provides
    @Singleton
    fun provideChatApiClient(chatApi: ChatApi): ChatClient {
        return ChatClient(chatApi)
    }

    @Provides
    @Singleton
    fun provideFcmApi(@BaseClient retrofit: Retrofit): FcmApi {
        return retrofit.create(FcmApi::class.java)
    }

    @Provides
    @Singleton
    fun provideFcmClient(fcmApi: FcmApi): FcmClient {
        return FcmClient(fcmApi)
    }

    @Provides
    @Singleton
    fun provideNotificationApi(@BaseClient retrofit: Retrofit): NotificationApi {
        return retrofit.create(NotificationApi::class.java)
    }

    @Provides
    @Singleton
    fun provideNotificationClient(notificationApi: NotificationApi): NotificationClient {
        return NotificationClient(notificationApi)
    }

    @Provides
    @Singleton
    fun provideWebSocketManager(
        gson: Gson
    ): WebSocketManager = WebSocketManager(gson)

    @Provides
    @Singleton
    fun provideGson(): Gson {
        return GsonBuilder()
            .registerTypeAdapterFactory(ApiResponseAdapterFactory())
            .create()
    }
}