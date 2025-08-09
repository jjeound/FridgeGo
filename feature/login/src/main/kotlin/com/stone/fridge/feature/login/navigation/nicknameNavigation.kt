package com.stone.fridge.feature.login.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.stone.fridge.core.navigation.GoScreen
import com.stone.fridge.feature.login.NickNameScreen
import kotlinx.serialization.Serializable

@Serializable data object NicknameRoute: GoScreen

fun NavGraphBuilder.nicknameNavigation(
    onShowSnackbar: suspend (String, String?) -> Unit,
) {
    composable<NicknameRoute> {
        NickNameScreen(
            onShowSnackbar = onShowSnackbar
        )
    }
}