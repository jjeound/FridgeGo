package com.stone.fridge.feature.chat.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.stone.fridge.core.navigation.GoScreen
import com.stone.fridge.feature.chat.ChattingScreen
import kotlinx.serialization.Serializable

@Serializable data object ChatRoute: GoScreen

@Serializable data object ChatBaseRoute

fun NavGraphBuilder.chatNavigation(
    isUnread: Boolean,
    navigateToNotification: () -> Unit,
    onShowSnackbar: suspend (String, String?) -> Unit,
    chatDestination: NavGraphBuilder.() -> Unit
) {
    navigation<ChatBaseRoute>(startDestination = ChatRoute) {
        composable<ChatRoute>{
            ChattingScreen(
                isUnread = isUnread,
                navigateToNotification = navigateToNotification,
                onShowSnackbar = onShowSnackbar
            )
        }
        chatDestination()
    }
}
