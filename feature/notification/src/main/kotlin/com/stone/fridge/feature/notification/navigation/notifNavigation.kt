package com.stone.fridge.feature.notification.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.stone.fridge.core.navigation.GoScreen
import com.stone.fridge.feature.notification.NotificationScreen
import kotlinx.serialization.Serializable

@Serializable data object NotifRoute: GoScreen

fun NavController.navigateToNotification() {
    navigate(NotifRoute) {
    }
}

fun NavGraphBuilder.notifNavigation(
    updateUnreadNotification : () -> Unit,
    onShowSnackbar: suspend (String, String?) -> Unit,
){
    composable<NotifRoute> {
        NotificationScreen(
            updateUnreadNotification = updateUnreadNotification,
            onShowSnackbar = onShowSnackbar
        )
    }
}