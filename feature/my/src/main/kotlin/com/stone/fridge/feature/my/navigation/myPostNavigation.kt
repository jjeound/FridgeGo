package com.stone.fridge.feature.my.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.stone.fridge.core.navigation.GoScreen
import com.stone.fridge.feature.my.etc.MyPostScreen
import kotlinx.serialization.Serializable

@Serializable
data class MyPostsRoute(val type: Boolean): GoScreen

fun NavGraphBuilder.myPostNavigation(
    navigateToPostDetail: (Long) -> Unit,
) {
    composable<MyPostsRoute>{
        MyPostScreen(
            navigateToPostDetail = navigateToPostDetail
        )
    }
}
