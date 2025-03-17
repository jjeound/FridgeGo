package com.example.untitled_capstone.di

import com.example.untitled_capstone.domain.repository.FridgeRepository
import com.example.untitled_capstone.domain.use_case.fridge.AddFridgeItem
import com.example.untitled_capstone.domain.use_case.fridge.DeleteFridgeItem
import com.example.untitled_capstone.domain.use_case.fridge.FridgeUseCases
import com.example.untitled_capstone.domain.use_case.fridge.GetFridgeItems
import com.example.untitled_capstone.domain.use_case.fridge.InvalidatePagingSource
import com.example.untitled_capstone.domain.use_case.fridge.ModifyFridgeItems
import com.example.untitled_capstone.domain.use_case.fridge.ToggleNotification
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object UseCaseModule {

    @Provides
    @Singleton
    fun provideFridgeUseCases(repository: FridgeRepository): FridgeUseCases {
        return FridgeUseCases(
            addFridgeItem = AddFridgeItem(repository),
            deleteFridgeItem = DeleteFridgeItem(repository),
            toggleNotification = ToggleNotification(repository),
            modifyFridgeItems = ModifyFridgeItems(repository),
            getFridgeItems = GetFridgeItems(repository),
            invalidatePagingSource = InvalidatePagingSource(repository)
        )
    }
}