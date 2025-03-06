package com.example.untitled_capstone.presentation.feature.home.event

import com.example.untitled_capstone.domain.model.Recipe

sealed interface HomeAction {
    data object LoadItems : HomeAction
    data class AddNewRecipe(val recipe: Recipe) : HomeAction
    data class ToggleLike(val id: Int) : HomeAction
}