package com.example.untitled_capstone.presentation.feature.home

import com.example.untitled_capstone.domain.model.Recipe
import java.io.File

sealed interface HomeEvent {
    data class ToggleLike(val id: Long, val liked: Boolean) : HomeEvent
    data class AddRecipe(val recipe: String) : HomeEvent
    data object GetRecipes: HomeEvent
    data class SetTastePreference(val tastePreference: String): HomeEvent
    data object GetTastePreference: HomeEvent
    data object GetRecipeByAi: HomeEvent
    data class GetRecipeById(val id: Long): HomeEvent
    data object InitState: HomeEvent
    data class DeleteRecipe(val id: Long): HomeEvent
    data class ModifyRecipe(val recipe: Recipe): HomeEvent
    data class UploadImage(val id: Long, val file: File): HomeEvent
}