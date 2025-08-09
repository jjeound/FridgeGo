package com.stone.fridge.feature.home.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptionsBuilder
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.stone.fridge.core.navigation.GoBaseRoute
import com.stone.fridge.core.navigation.GoScreen
import com.stone.fridge.feature.home.HomeScreen
import kotlinx.serialization.Serializable

@Serializable data object HomeRoute: GoScreen

@Serializable data object HomeBaseRoute: GoBaseRoute

fun NavController.navigateToHome(navOptions: NavOptionsBuilder.() -> Unit = {}) {
    navigate(route = HomeBaseRoute) {
        navOptions()
    }
}

fun NavGraphBuilder.homeNavigation(
    shouldShowBottomSheet: Boolean,
    hideBottomSheet: () -> Unit,
    isUnread: Boolean,
    navigateToNotification: () -> Unit,
    onShowSnackbar: suspend (String, String?) -> Unit,
    homeDestination: NavGraphBuilder.() -> Unit,
) {
    navigation<HomeBaseRoute>(startDestination = HomeRoute) {
        composable<HomeRoute>{
            HomeScreen(
                isUnread = isUnread,
                shouldShowBottomSheet = shouldShowBottomSheet,
                hideBottomSheet = hideBottomSheet,
                navigateToNotification = navigateToNotification,
                onShowSnackbar = onShowSnackbar,
            )
        }
        homeDestination()
    }
}
