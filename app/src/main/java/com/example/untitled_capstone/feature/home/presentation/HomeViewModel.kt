package com.example.untitled_capstone.feature.home.presentation

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.untitled_capstone.R
import com.example.untitled_capstone.feature.home.domain.model.Recipe
import com.example.untitled_capstone.feature.home.presentation.state.MyRecipeState

class HomeViewModel: ViewModel(){
    var state by mutableStateOf(MyRecipeState())
        private set

    init {
        state = state.copy(
            recipeItems = listOf(
                Recipe(
                    title = "title1",
                    image = R.drawable.ic_launcher_background,
                    ingredients = listOf("ingredient1", "ingredient2"),
                    steps = listOf("step1", "step2"),
                    isLiked = false
                ),
                Recipe(
                    title = "title2",
                    image = R.drawable.ic_launcher_background,
                    ingredients = listOf("ingredient1", "ingredient2"),
                    steps = listOf("step1", "step2"),
                    isLiked = false
                ),
                Recipe(
                    title = "title3",
                    image = null,
                    ingredients = listOf("ingredient1", "ingredient2"),
                    steps = listOf("step1", "step2"),
                    isLiked = false
                ),
                Recipe(
                    title = "title4",
                    image = null,
                    ingredients = listOf("ingredient1", "ingredient2"),
                    steps = listOf("step1", "step2"),
                    isLiked = false
                )
            ),
            isLoading = false
        )
    }
}