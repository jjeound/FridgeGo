package com.stone.fridge.feature.post.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.stone.fridge.core.model.Post
import com.stone.fridge.core.navigation.GoScreen
import com.stone.fridge.core.navigation.PostNavType
import com.stone.fridge.feature.post.crud.PostCRUDScreen
import kotlinx.serialization.Serializable
import kotlin.reflect.typeOf

@Serializable
data class PostCRUDRoute(val post: Post?): GoScreen{
    companion object {
        val typeMap = mapOf(typeOf<Post?>() to PostNavType)
    }
}

fun NavController.navigateToPostCRUD(){
    navigate(PostCRUDRoute){}
}
fun NavGraphBuilder.postCRUDNavigation(
    onShowSnackbar: suspend (String, String?) -> Unit,
) {
    composable<PostCRUDRoute>(
        typeMap = PostCRUDRoute.typeMap
    ){
        PostCRUDScreen(
            onShowSnackbar = onShowSnackbar
        )
    }
}
