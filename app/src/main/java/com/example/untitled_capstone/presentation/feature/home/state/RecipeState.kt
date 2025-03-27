package com.example.untitled_capstone.presentation.feature.home.state

import com.example.untitled_capstone.domain.model.Recipe

data class RecipeState(
    val item : Recipe? = null,
    val loading: Boolean = false,
    val error: String? = null
)