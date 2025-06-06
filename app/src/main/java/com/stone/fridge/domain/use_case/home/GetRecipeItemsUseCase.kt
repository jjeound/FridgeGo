package com.stone.fridge.domain.use_case.home

import androidx.paging.PagingData
import androidx.paging.map
import com.stone.fridge.domain.model.RecipeRaw
import com.stone.fridge.domain.repository.HomeRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetRecipeItemsUseCase @Inject constructor(
    private val repository: HomeRepository
) {
    operator fun invoke(): Flow<PagingData<RecipeRaw>> {
        return repository.getRecipes().map { pagingData ->
            pagingData.map { it.toRecipe() }
        }
    }
}
