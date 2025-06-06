package com.stone.fridge.presentation.feature.home

sealed interface HomeEvent {
    data class ToggleLike(val id: Long, val liked: Boolean) : HomeEvent
    data class AddRecipe(val recipe: String) : HomeEvent
    data object GetRecipes: HomeEvent
    data class SetTastePreference(val tastePreference: String): HomeEvent
    data object GetTastePreference: HomeEvent
    data object GetRecipeByAi: HomeEvent
}