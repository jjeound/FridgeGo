package com.example.untitled_capstone.di

import android.content.Context
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.room.Room
import com.example.untitled_capstone.core.util.Constants.BASE_URL
import com.example.untitled_capstone.data.local.db.FridgeItemDatabase
import com.example.untitled_capstone.data.local.entity.FridgeItemEntity
import com.example.untitled_capstone.data.local.remote.FridgeItemDao
import com.example.untitled_capstone.data.pagination.FridgePagingSource
import com.example.untitled_capstone.data.remote.service.FridgeApi
import com.example.untitled_capstone.data.remote.service.LoginApi
import com.example.untitled_capstone.data.remote.service.MyApi
import com.example.untitled_capstone.data.remote.service.TokenApi
import com.example.untitled_capstone.domain.repository.TokenRepository
import com.example.untitled_capstone.domain.use_case.token.AuthAuthenticator
import com.example.untitled_capstone.domain.use_case.token.AuthInterceptor
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
    fun provideFridgeItemDatabase(@ApplicationContext context: Context): FridgeItemDatabase{
        return Room.databaseBuilder(
            context, FridgeItemDatabase::class.java, "fridge_item_database"
        ).build()
    }

    @Provides
    @Singleton
    fun provideDao(db: FridgeItemDatabase): FridgeItemDao = db.dao

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
    fun provideFridgeApi(okHttpClient: OkHttpClient): FridgeApi {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(FridgeApi::class.java)
    }

    @Provides
    @Singleton
    fun provideLoginApi(): LoginApi {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(LoginApi::class.java)
    }

    @Provides
    @Singleton
    fun provideMyApi(okHttpClient: OkHttpClient): MyApi {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(MyApi::class.java)
    }

    @Provides
    @Singleton
    fun provideTokenApi(): TokenApi {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(TokenApi::class.java)
    }

    @OptIn(ExperimentalPagingApi::class)
    @Provides
    @Singleton
    fun provideFridgePager(db: FridgeItemDatabase, api: FridgeApi, sort: String): Pager<Int, FridgeItemEntity> {
        return Pager(
            config = PagingConfig(pageSize = 10),
            remoteMediator = FridgePagingSource(
                db = db,
                api = api,
                sort = sort
            ),
            pagingSourceFactory = {
                db.dao.getFridgeItems()
            }
        )
    }
}