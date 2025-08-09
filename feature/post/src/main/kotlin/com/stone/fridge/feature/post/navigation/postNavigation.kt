package com.stone.fridge.feature.post.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.stone.fridge.core.navigation.GoBaseRoute
import com.stone.fridge.core.navigation.GoScreen
import com.stone.fridge.feature.post.PostScreen
import kotlinx.serialization.Serializable

@Serializable
data object PostRoute: GoScreen

@Serializable
data object PostBaseRoute: GoBaseRoute

fun NavGraphBuilder.postNavigation(
    isUnread: Boolean,
    onShowSnackbar: suspend (String, String?) -> Unit,
    navigateToNotification: () -> Unit,
    postDestination: NavGraphBuilder.() -> Unit
) {
    navigation<PostBaseRoute>(startDestination = PostRoute) {
        composable<PostRoute>{
            PostScreen(
                isUnread = isUnread,
                navigateToNotification = navigateToNotification,
                onShowSnackbar = onShowSnackbar
            )
        }
        postDestination()
    }
}
