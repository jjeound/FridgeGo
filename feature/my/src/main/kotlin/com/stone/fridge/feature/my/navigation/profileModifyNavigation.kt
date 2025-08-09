package com.stone.fridge.feature.my.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.stone.fridge.core.navigation.GoScreen
import com.stone.fridge.feature.my.profile.ProfileModifyScreen
import kotlinx.serialization.Serializable

@Serializable
data object ProfileModifyRoute: GoScreen

fun NavGraphBuilder.profileModifyNavigation(
    onShowSnackbar: suspend (String, String?) -> Unit,
) {
    composable<ProfileModifyRoute>{
        ProfileModifyScreen(
            onShowSnackbar = onShowSnackbar,
        )
    }
}