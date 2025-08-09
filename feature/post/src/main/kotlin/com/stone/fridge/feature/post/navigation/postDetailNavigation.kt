package com.stone.fridge.feature.post.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.stone.fridge.core.navigation.GoScreen
import com.stone.fridge.feature.post.detail.PostDetailScreen
import kotlinx.serialization.Serializable

@Serializable
data class PostDetailRoute(val postId: Long): GoScreen

fun NavController.navigateToPostDetail(postId: Long) {
    navigate(PostDetailRoute(postId)){

    }
}

fun NavGraphBuilder.postDetailNavigation(
    onShowSnackbar: suspend (String, String?) -> Unit,
    navigateToChattingRoom: (Long, Boolean) -> Unit
) {
    composable<PostRoute>{
        PostDetailScreen(
            onShowSnackbar = onShowSnackbar,
            navigateToChattingRoom = navigateToChattingRoom
        )
    }
}
