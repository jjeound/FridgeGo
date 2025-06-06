package com.stone.fridge.domain.use_case.home

import com.stone.fridge.core.util.Resource
import com.stone.fridge.domain.repository.HomeRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject


class RecipeToggleLikeUseCase @Inject constructor(
    private val repository: HomeRepository
) {
    operator fun invoke(id: Long, liked: Boolean): Flow<Resource<Boolean>> {
        return repository.toggleLike(id, liked)
    }
}