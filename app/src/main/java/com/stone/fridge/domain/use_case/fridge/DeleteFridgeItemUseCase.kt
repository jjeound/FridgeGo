package com.stone.fridge.domain.use_case.fridge

import com.stone.fridge.core.util.Resource
import com.stone.fridge.domain.repository.FridgeRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class DeleteFridgeItemUseCase @Inject constructor(
    private val fridgeRepository: FridgeRepository
) {
    operator fun invoke(id: Long): Flow<Resource<String>> {
        return fridgeRepository.deleteItem(id)
    }
}