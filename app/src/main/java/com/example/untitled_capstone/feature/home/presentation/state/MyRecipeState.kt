package com.example.untitled_capstone.feature.home.presentation.state

import com.example.untitled_capstone.feature.home.domain.model.Recipe

data class MyRecipeState(
    val recipeItems: List<Recipe> = emptyList(),
    val isLoading: Boolean = false
)