package com.example.untitled_capstone.di

import android.content.Context
import com.example.untitled_capstone.data.remote.service.TokenApi
import com.example.untitled_capstone.data.repository.TokenRepositoryImpl
import com.example.untitled_capstone.domain.repository.TokenRepository
import com.example.untitled_capstone.domain.use_case.token.AuthAuthenticator
import com.example.untitled_capstone.domain.use_case.token.AuthInterceptor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object TokenModule {

    @Provides
    @Singleton
    fun provideTokenRepository(@ApplicationContext context: Context, api: TokenApi): TokenRepository {
        return TokenRepositoryImpl(context, api)
    }

    @Provides
    fun provideAuthInterceptor(repository: TokenRepository): AuthInterceptor {
        return AuthInterceptor(repository)
    }

    @Provides
    fun provideAuthAuthenticator(repository: TokenRepository): AuthAuthenticator {
        return AuthAuthenticator(repository)
    }
}