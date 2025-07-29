package com.stone.fridge.domain.use_case.fridge

import com.stone.fridge.core.util.Resource
import com.stone.fridge.domain.model.NewFridge
import com.stone.fridge.domain.repository.FridgeRepository
import kotlinx.coroutines.flow.Flow
import java.io.File
import javax.inject.Inject

class AddFridgeItemUseCase @Inject constructor(
    private val fridgeRepository: FridgeRepository
) {
    operator fun invoke(item: NewFridge, image: File?): Flow<Resource<String>> {
        return fridgeRepository.addItem(item, image)
    }
}