package com.stone.fridge.domain.use_case.home

import com.stone.fridge.core.util.Resource
import com.stone.fridge.domain.model.Recipe
import com.stone.fridge.domain.repository.HomeRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ModifyRecipeUseCase @Inject constructor(
    private val repository: HomeRepository
) {
    operator fun invoke(recipe: Recipe): Flow<Resource<String>> {
        return repository.modifyRecipe(recipe)
    }
}