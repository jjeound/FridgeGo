package com.stone.fridge.domain.use_case.fridge

import com.stone.fridge.core.util.Resource
import com.stone.fridge.domain.model.FridgeItem
import com.stone.fridge.domain.repository.FridgeRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetFridgeItemByIdUseCase @Inject constructor(
    private val fridgeRepository: FridgeRepository
) {
    operator fun invoke(id: Long): Flow<Resource<FridgeItem>>{
        return fridgeRepository.getFridgeItemById(id)
    }
}