package com.stone.fridge.feature.login.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.stone.fridge.core.navigation.GoScreen
import com.stone.fridge.feature.login.LocationScreen
import kotlinx.serialization.Serializable

@Serializable data class LocationRoute(val from: Boolean): GoScreen

fun NavController.navigateToLocation(from: Boolean) {
    navigate(LocationRoute(from)){}
}

fun NavGraphBuilder.locationNavigation(
    onShowSnackbar: suspend (String, String?) -> Unit,
    onLocationSet: () -> Unit,
) {
    composable<LocationRoute> {

        LocationScreen(
            onShowSnackbar = onShowSnackbar,
            onLocationSet = onLocationSet,
            from = it.toRoute<LocationRoute>().from
        )
    }
}