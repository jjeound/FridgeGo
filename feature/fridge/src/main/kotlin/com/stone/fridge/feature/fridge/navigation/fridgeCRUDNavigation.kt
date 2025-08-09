package com.stone.fridge.feature.fridge.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.stone.fridge.core.navigation.GoScreen
import com.stone.fridge.feature.fridge.crud.FridgeCRUDScreen
import kotlinx.serialization.Serializable

@Serializable data class FridgeCRUDRoute(val id: Long? = null): GoScreen

fun NavController.navigateToFridgeCRUD(
    id: Long? = null,
) {
    navigate(FridgeCRUDRoute(id))
}

fun NavGraphBuilder.fridgeCRUDNavigation(
    onShowSnackbar: suspend (String, String?) -> Unit,
) {
    composable<FridgeCRUDRoute>{
        FridgeCRUDScreen(
            onShowSnackbar = onShowSnackbar,
        )
    }
}
