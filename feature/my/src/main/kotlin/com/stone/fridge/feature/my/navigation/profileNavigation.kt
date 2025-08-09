package com.stone.fridge.feature.my.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.stone.fridge.core.navigation.GoScreen
import com.stone.fridge.feature.my.profile.ProfileScreen
import kotlinx.serialization.Serializable

@Serializable
data object ProfileRoute: GoScreen

fun NavGraphBuilder.profileNavigation(
    onLogout: () -> Unit,
    onShowSnackbar: suspend (String, String?) -> Unit,
) {
    composable<ProfileRoute>{
        ProfileScreen(
            onLogout = onLogout,
            onShowSnackbar = onShowSnackbar
        )
    }
}