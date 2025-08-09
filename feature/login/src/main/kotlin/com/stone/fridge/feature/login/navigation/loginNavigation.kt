package com.stone.fridge.feature.login.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.stone.fridge.core.navigation.GoBaseRoute
import com.stone.fridge.core.navigation.GoScreen
import com.stone.fridge.feature.login.LoginScreen
import kotlinx.serialization.Serializable

@Serializable data object LoginRoute: GoScreen

@Serializable data object LoginBaseRoute: GoBaseRoute

fun NavController.navigateToLogin() {
    navigate(route = LoginRoute) {
        popUpTo(0) { inclusive = true }
        launchSingleTop = true
    }
}

fun NavGraphBuilder.loginNavigation(
    onShowSnackbar: suspend (String, String?) -> Unit,
    onLogin: () -> Unit,
    loginDestination: NavGraphBuilder.() -> Unit,
) {
    navigation<LoginBaseRoute>(startDestination = LoginRoute){
        composable<LoginRoute> {
            LoginScreen(
                onShowSnackbar = onShowSnackbar,
                onLogin = onLogin
            )
        }
        loginDestination()
    }
}