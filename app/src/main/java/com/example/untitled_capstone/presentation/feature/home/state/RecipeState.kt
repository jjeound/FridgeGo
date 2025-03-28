package com.example.untitled_capstone.presentation.feature.home.state

import com.example.untitled_capstone.domain.model.RecipeRaw

data class RecipeState(
    val recipe : RecipeRaw? = null,
    val loading: Boolean = false,
    val error: String? = null
)