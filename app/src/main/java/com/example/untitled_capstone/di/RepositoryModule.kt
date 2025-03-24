package com.example.untitled_capstone.di

import android.app.Application
import com.example.untitled_capstone.data.local.db.FridgeItemDatabase
import com.example.untitled_capstone.data.local.db.RecipeItemDatabase
import com.example.untitled_capstone.data.remote.service.FridgeApi
import com.example.untitled_capstone.data.remote.service.HomeApi
import com.example.untitled_capstone.data.remote.service.LoginApi
import com.example.untitled_capstone.data.remote.service.MyApi
import com.example.untitled_capstone.data.remote.service.ShoppingApi
import com.example.untitled_capstone.data.repository.FridgeRepositoryImpl
import com.example.untitled_capstone.data.repository.HomeRepositoryImpl
import com.example.untitled_capstone.data.repository.LocalUserMangerImpl
import com.example.untitled_capstone.data.repository.LoginRepositoryImpl
import com.example.untitled_capstone.data.repository.MyRepositoryImpl
import com.example.untitled_capstone.data.repository.ShoppingRepositoryImpl
import com.example.untitled_capstone.domain.repository.FridgeRepository
import com.example.untitled_capstone.domain.repository.HomeRepository
import com.example.untitled_capstone.domain.repository.LocalUserManger
import com.example.untitled_capstone.domain.repository.LoginRepository
import com.example.untitled_capstone.domain.repository.MyRepository
import com.example.untitled_capstone.domain.repository.ShoppingRepository
import com.example.untitled_capstone.domain.repository.TokenRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun provideHomeRepository(db: RecipeItemDatabase, api: HomeApi): HomeRepository{
        return HomeRepositoryImpl(api, db)
    }

    @Provides
    @Singleton
    fun provideShoppingRepository(api: ShoppingApi): ShoppingRepository{
        return ShoppingRepositoryImpl(api)
    }

    @Provides
    @Singleton
    fun provideFridgeRepository(db: FridgeItemDatabase, api: FridgeApi): FridgeRepository{
        return FridgeRepositoryImpl(api, db)
    }

    @Provides
    @Singleton
    fun provideLoginRepository(api: LoginApi, tokenRepository: TokenRepository): LoginRepository{
        return LoginRepositoryImpl(api, tokenRepository)
    }

    @Provides
    @Singleton
    fun provideMyRepository(api: MyApi, tokenRepository: TokenRepository): MyRepository{
        return MyRepositoryImpl(api, tokenRepository)
    }

    @Provides
    @Singleton
    fun provideLocalUserManager(application: Application): LocalUserManger{
        return LocalUserMangerImpl(application)
    }
}