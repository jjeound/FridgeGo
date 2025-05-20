package com.example.untitled_capstone.domain.use_case.home

import com.example.untitled_capstone.core.util.Resource
import com.example.untitled_capstone.domain.model.Recipe
import com.example.untitled_capstone.domain.repository.HomeRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ModifyRecipeUseCase @Inject constructor(
    private val repository: HomeRepository
) {
    suspend operator fun invoke(recipe: Recipe): Flow<Resource<String>> {
        return repository.modifyRecipe(recipe)
    }
}