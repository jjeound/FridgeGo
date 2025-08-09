package com.stone.fridge.feature.my.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.stone.fridge.core.navigation.GoScreen
import com.stone.fridge.feature.my.MyScreen
import kotlinx.serialization.Serializable

@Serializable
data object MyRoute: GoScreen

@Serializable
data object MyBaseRoute

fun NavGraphBuilder.myNavigation(
    onShowSnackbar: suspend (String, String?) -> Unit,
    navigateToLocation: (Boolean) -> Unit,
    myDestination: NavGraphBuilder.() -> Unit,
) {
    navigation<MyBaseRoute>(startDestination = MyRoute) {
        composable<MyRoute>{
            MyScreen(
                onShowSnackbar = onShowSnackbar,
                navigateToLocation = navigateToLocation
            )
        }
        myDestination()
    }
}
