package com.example.untitled_capstone.di

import com.example.untitled_capstone.data.local.db.FridgeItemDatabase
import com.example.untitled_capstone.data.local.remote.FridgeItemDao
import com.example.untitled_capstone.data.remote.Api
import com.example.untitled_capstone.data.repository.FridgeRepositoryImpl
import com.example.untitled_capstone.domain.repository.FridgeRepository
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
    fun provideFridgeRepository(db: FridgeItemDatabase, api: Api): FridgeRepository{
        return FridgeRepositoryImpl(api, db)
    }
}