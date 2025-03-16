package com.example.untitled_capstone.domain.use_case.fridge

import androidx.paging.PagingData
import com.example.untitled_capstone.domain.model.FridgeItem
import com.example.untitled_capstone.domain.repository.FridgeRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetFridgeItem @Inject constructor(
    private val fridgeRepository: FridgeRepository
) {
    operator fun invoke(): Flow<PagingData<FridgeItem>> {
        return fridgeRepository.getFridgeItemsPaged()
    }
}
