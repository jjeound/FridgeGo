package com.example.untitled_capstone.di

import com.example.untitled_capstone.data.local.db.FridgeItemDatabase
import com.example.untitled_capstone.data.local.remote.FridgeItemDao
import com.example.untitled_capstone.data.remote.Api
import com.example.untitled_capstone.data.repository.FridgeRepositoryImpl
import com.example.untitled_capstone.domain.repository.FridgeRepository
import com.example.untitled_capstone.domain.use_case.fridge.AddFridgeItem
import com.example.untitled_capstone.domain.use_case.fridge.DeleteFridgeItem
import com.example.untitled_capstone.domain.use_case.fridge.FridgeUseCases
import com.example.untitled_capstone.domain.use_case.fridge.GetFridgeItem
import com.example.untitled_capstone.domain.use_case.fridge.ModifyFridgeItems
import com.example.untitled_capstone.domain.use_case.fridge.ToggleNotification
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun provideFridgeRepository(db: FridgeItemDatabase, api: Api, dao: FridgeItemDao): FridgeRepository{
        return FridgeRepositoryImpl(api, db, dao)
    }
}