package com.example.untitled_capstone.domain.use_case.home

import com.example.untitled_capstone.core.util.Resource
import com.example.untitled_capstone.data.remote.dto.ApiResponse
import com.example.untitled_capstone.domain.model.Recipe
import com.example.untitled_capstone.domain.repository.HomeRepository
import javax.inject.Inject

class ModifyRecipe @Inject constructor(
    private val repository: HomeRepository
) {
    suspend operator fun invoke(recipe: Recipe): Resource<ApiResponse> {
        return repository.modifyRecipe(recipe)
    }
}