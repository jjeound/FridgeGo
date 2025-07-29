package com.stone.fridge.domain.use_case.home

import com.stone.fridge.core.util.Resource
import com.stone.fridge.domain.repository.HomeRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetFirstRecommendationUseCase @Inject constructor(
    private val repository: HomeRepository
) {
    operator fun invoke(): Flow<Resource<String>> {
        return repository.getFirstRecommendation()
    }
}