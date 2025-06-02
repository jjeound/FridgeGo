package com.example.untitled_capstone.domain.use_case.fridge

import com.example.untitled_capstone.core.util.Resource
import com.example.untitled_capstone.domain.model.FridgeItem
import com.example.untitled_capstone.domain.repository.FridgeRepository
import kotlinx.coroutines.flow.Flow
import java.io.File
import javax.inject.Inject

class ModifyFridgeItemUseCase @Inject constructor(
    private val fridgeRepository: FridgeRepository
) {
    operator fun invoke(item: FridgeItem, image: File?, isOriginalImageDeleted: Boolean): Flow<Resource<String>> {
        return fridgeRepository.modifyItem(item, image, isOriginalImageDeleted)
    }
}