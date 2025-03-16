package com.example.untitled_capstone.di

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
import javax.inject.Singleton


@Module
@InstallIn(ViewModelComponent::class)
object UseCaseModule {

    @Provides
    @Singleton
    fun provideFridgeUseCases(repository: FridgeRepository): FridgeUseCases {
        return FridgeUseCases(
            addFridgeItem = AddFridgeItem(repository),
            deleteFridgeItem = DeleteFridgeItem(repository),
            toggleNotification = ToggleNotification(repository),
            modifyFridgeItems = ModifyFridgeItems(repository),
            getFridgeItem = GetFridgeItem(repository)
        )
    }
}