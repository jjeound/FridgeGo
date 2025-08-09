package com.stone.fridge.feature.my.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.stone.fridge.core.navigation.GoScreen
import com.stone.fridge.feature.my.AppInfoScreen
import kotlinx.serialization.Serializable

@Serializable
data object AppInfoRoute: GoScreen

fun NavGraphBuilder.appInfoNavigation(
) {
    composable<AppInfoRoute>{
        AppInfoScreen()
    }
}