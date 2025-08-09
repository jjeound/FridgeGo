package com.stone.fridge.feature.post.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.stone.fridge.core.navigation.GoScreen
import com.stone.fridge.feature.post.profile.PostProfileScreen
import kotlinx.serialization.Serializable

@Serializable
data class PostProfileRoute(val userName: String): GoScreen

fun NavGraphBuilder.postProfileNavigation(
    onShowSnackbar: suspend (String, String?) -> Unit,
) {
    composable<PostProfileRoute>{
        PostProfileScreen(
            onShowSnackbar = onShowSnackbar,
        )
    }
}