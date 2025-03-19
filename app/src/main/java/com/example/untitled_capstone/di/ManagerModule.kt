package com.example.untitled_capstone.di

import com.example.untitled_capstone.data.repository.TokenManagerImpl
import com.example.untitled_capstone.domain.repository.TokenManager
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class MangerModule {

    @Binds
    @Singleton
    abstract fun bindTokenManger(tokenManagerImpl: TokenManagerImpl) : TokenManager
}