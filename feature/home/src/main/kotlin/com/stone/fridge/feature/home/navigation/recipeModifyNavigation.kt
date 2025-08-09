package com.stone.fridge.feature.home.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.stone.fridge.core.navigation.GoScreen
import com.stone.fridge.feature.home.detail.RecipeModifyScreen
import kotlinx.serialization.Serializable

@Serializable data class RecipeModifyNav(val id: Long): GoScreen

fun NavGraphBuilder.recipeModifyNavigation(
    onShowSnackbar: suspend (String, String?) -> Unit,
) {
    composable<RecipeModifyNav>{
        RecipeModifyScreen(
            onShowSnackbar = onShowSnackbar
        )
    }
}
