package com.stone.fridge.domain.use_case.home

import com.stone.fridge.domain.repository.HomeRepository
import javax.inject.Inject

class SetIsFirstSelectionUseCase @Inject constructor(
    private val repository: HomeRepository
) {
    operator fun invoke(isFirst: Boolean) {
        repository.setFirstSelection(isFirst)
    }
}