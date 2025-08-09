package com.stone.fridge.feature.post.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.stone.fridge.core.navigation.GoScreen
import com.stone.fridge.feature.post.search.PostSearchScreen
import kotlinx.serialization.Serializable

@Serializable
data object PostSearchRoute: GoScreen

fun NavGraphBuilder.postSearchNavigation(
    onShowSnackbar: suspend (String, String?) -> Unit,
) {
    composable<PostSearchRoute>{
        PostSearchScreen(
            onShowSnackbar = onShowSnackbar
        )
    }
}
