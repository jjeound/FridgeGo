package com.stone.fridge.feature.fridge.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.stone.fridge.core.navigation.GoScreen
import com.stone.fridge.feature.fridge.FridgeScreen
import kotlinx.serialization.Serializable

@Serializable data object FridgeRoute: GoScreen

@Serializable data object FridgeBaseRoute

fun NavGraphBuilder.fridgeNavigation(
    isUnread: Boolean,
    navigateToNotification: () -> Unit,
    onShowSnackbar: suspend (String, String?) -> Unit,
    fridgeDestination: NavGraphBuilder.() -> Unit,
) {
    navigation<FridgeBaseRoute>(startDestination = FridgeRoute) {
        composable<FridgeRoute>{
            FridgeScreen(
                isUnread = isUnread,
                navigateToNotification = navigateToNotification,
                onShowSnackbar = onShowSnackbar,
            )
        }
        fridgeDestination()
    }
}
