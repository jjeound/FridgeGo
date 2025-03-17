package com.example.untitled_capstone.domain.use_case.fridge

import com.example.untitled_capstone.domain.repository.FridgeRepository
import javax.inject.Inject

class InvalidatePagingSource @Inject constructor(
    private val fridgeRepository: FridgeRepository
) {
    operator fun invoke() {
        fridgeRepository.invalidatePagingSource()
    }
}