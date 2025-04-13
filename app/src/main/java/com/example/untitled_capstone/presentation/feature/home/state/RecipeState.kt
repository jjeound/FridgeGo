package com.example.untitled_capstone.presentation.feature.home.state

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.example.untitled_capstone.domain.model.Recipe

class RecipeState{
    var recipe by mutableStateOf<Recipe?>(null)
    var isLoading by mutableStateOf(false)
}