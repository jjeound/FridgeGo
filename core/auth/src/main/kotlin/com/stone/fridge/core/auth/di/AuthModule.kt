package com.stone.fridge.core.auth.di

import com.stone.fridge.core.auth.TokenDataSource
import com.stone.fridge.core.auth.TokenDataSourceImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
internal interface AuthModule {
    @Binds
    fun bindsTokenDataSource(tokenDataSourceImpl: TokenDataSourceImpl): TokenDataSource
}