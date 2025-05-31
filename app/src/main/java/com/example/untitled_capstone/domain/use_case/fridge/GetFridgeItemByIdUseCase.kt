package com.example.untitled_capstone.domain.use_case.fridge

import com.example.untitled_capstone.core.util.Resource
import com.example.untitled_capstone.domain.model.FridgeItem
import com.example.untitled_capstone.domain.repository.FridgeRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetFridgeItemByIdUseCase @Inject constructor(
    private val fridgeRepository: FridgeRepository
) {
    operator fun invoke(id: Long): Flow<Resource<FridgeItem>>{
        return fridgeRepository.getFridgeItemById(id)
    }
}