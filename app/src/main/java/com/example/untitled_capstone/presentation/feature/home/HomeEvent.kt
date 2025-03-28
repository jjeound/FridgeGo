package com.example.untitled_capstone.presentation.feature.home

sealed interface HomeEvent {
    data class ToggleLike(val id: Long, val liked: Boolean) : HomeEvent
    data class AddRecipe(val title: String, val instructions: String) : HomeEvent
    data object GetRecipes: HomeEvent
    data class SetTastePreference(val tastePreference: String): HomeEvent
    data object GetTastePreference: HomeEvent
    data object GetRecipeByAi: HomeEvent
    data class GetRecipeById(val id: Long): HomeEvent
}