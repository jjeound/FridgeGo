package com.stone.fridge.feature.home.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.stone.fridge.core.navigation.GoScreen
import com.stone.fridge.feature.home.detail.RecipeScreen
import kotlinx.serialization.Serializable

@Serializable data class RecipeNav(val id: Long): GoScreen

fun NavGraphBuilder.recipeNavigation(
    onShowSnackbar: suspend (String, String?) -> Unit,
) {
    composable<RecipeNav>{
        RecipeScreen(
            onShowSnackbar = onShowSnackbar,
        )
    }
}
