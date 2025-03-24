package com.example.untitled_capstone.domain.use_case.home

import androidx.paging.PagingData
import androidx.paging.map
import com.example.untitled_capstone.domain.model.FridgeItem
import com.example.untitled_capstone.domain.model.RecipeRaw
import com.example.untitled_capstone.domain.repository.FridgeRepository
import com.example.untitled_capstone.domain.repository.HomeRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetRecipeItems @Inject constructor(
    private val repository: HomeRepository
) {
    operator fun invoke(): Flow<PagingData<RecipeRaw>> {
        return repository.getRecipes().map { pagingData ->
            pagingData.map { it.toRecipe() }
        }
    }
}
