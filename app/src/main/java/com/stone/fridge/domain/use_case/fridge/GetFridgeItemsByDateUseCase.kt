package com.stone.fridge.domain.use_case.fridge

import androidx.paging.PagingData
import androidx.paging.map
import com.stone.fridge.data.util.FridgeFetchType
import com.stone.fridge.domain.model.FridgeItem
import com.stone.fridge.domain.repository.FridgeRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetFridgeItemsByDateUseCase @Inject constructor(
    private val fridgeRepository: FridgeRepository
) {
    operator fun invoke(fetchType: FridgeFetchType): Flow<PagingData<FridgeItem>> {
        return fridgeRepository.getFridgeItemsByDate(fetchType).map { pagingData ->
            pagingData.map { it.toFridgeItem() }
        }
    }
}
