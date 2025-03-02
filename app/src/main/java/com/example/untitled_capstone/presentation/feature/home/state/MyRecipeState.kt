package com.example.untitled_capstone.presentation.feature.home.state

import com.example.untitled_capstone.domain.model.Recipe

data class MyRecipeState(
    val recipeItems: List<Recipe> = emptyList(),
    val isLoading: Boolean = false
)