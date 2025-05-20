package com.example.untitled_capstone.domain.use_case.home

import com.example.untitled_capstone.domain.repository.HomeRepository
import javax.inject.Inject

class GetIsFirstSelectionUseCase @Inject constructor(
    private val repository: HomeRepository
) {
    operator fun invoke(): Boolean {
        return repository.isFirstSelection()
    }
}