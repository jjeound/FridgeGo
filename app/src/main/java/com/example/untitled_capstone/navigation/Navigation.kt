package com.example.untitled_capstone.navigation

import android.net.Uri
import android.os.Bundle
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.example.untitled_capstone.feature.notification.presentation.NotificationViewModel
import com.example.untitled_capstone.feature.chatting.domain.model.ChattingRoom
import com.example.untitled_capstone.feature.chatting.presentation.ChatViewModel
import com.example.untitled_capstone.feature.chatting.presentation.screen.ChattingRoomNav
import com.example.untitled_capstone.feature.chatting.presentation.screen.ChattingRoomScreen
import com.example.untitled_capstone.feature.chatting.presentation.screen.ChattingScreen
import com.example.untitled_capstone.feature.home.domain.model.Recipe
import com.example.untitled_capstone.feature.home.presentation.screen.HomeScreen
import com.example.untitled_capstone.feature.home.presentation.HomeViewModel
import com.example.untitled_capstone.feature.home.presentation.screen.RecipeNav
import com.example.untitled_capstone.feature.home.presentation.screen.RecipeScreen
import com.example.untitled_capstone.feature.main.BottomScreen
import com.example.untitled_capstone.feature.my.presentation.screen.MyScreen
import com.example.untitled_capstone.feature.notification.presentation.screen.NotificationNav
import com.example.untitled_capstone.feature.notification.presentation.screen.NotificationScreen
import com.example.untitled_capstone.feature.refrigerator.presentation.FridgeViewModel
import com.example.untitled_capstone.feature.refrigerator.presentation.screen.AddFridgeItemNav
import com.example.untitled_capstone.feature.refrigerator.presentation.screen.AddFridgeItemScreen
import com.example.untitled_capstone.feature.refrigerator.presentation.screen.RefrigeratorScreen
import com.example.untitled_capstone.feature.shopping.domain.model.Post
import com.example.untitled_capstone.feature.shopping.presentation.PostViewModel
import com.example.untitled_capstone.feature.shopping.presentation.screen.PostNav
import com.example.untitled_capstone.feature.shopping.presentation.screen.PostScreen
import com.example.untitled_capstone.feature.shopping.presentation.screen.ShoppingScreen
import com.example.untitled_capstone.feature.shopping.presentation.screen.WritingNav
import com.example.untitled_capstone.feature.shopping.presentation.screen.WritingNewPostScreen
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlin.reflect.typeOf


@Composable
fun Navigation(navController: NavHostController) {
     NavHost(navController = navController, startDestination = BottomScreen.Home.route){
         composable(BottomScreen.Home.route) {
             val viewModel = it.sharedViewModel<HomeViewModel>(navController)
             HomeScreen(viewModel.state, navController)
         }
         composable(BottomScreen.Shopping.route){
             val viewModel = PostViewModel()
             ShoppingScreen(navController = navController, viewModel.state)
         }
         composable(BottomScreen.Refrigerator.route){
             val viewModel = FridgeViewModel()
             RefrigeratorScreen(viewModel.state)
         }
         composable(BottomScreen.Chat.route){
             val viewModel = it.sharedViewModel<ChatViewModel>(navController)
             ChattingScreen(viewModel.chatState, navController)
         }
         composable(BottomScreen.My.route){
             MyScreen()
         }
         composable<RecipeNav>(
            typeMap = mapOf(
                typeOf<Recipe>() to CustomNavType.RecipeType
            )
         ) {
            val args = it.toRoute<RecipeNav>()
            RecipeScreen(
                args.recipe, navController
            )
         }
         composable<PostNav>(
             typeMap = mapOf(
                 typeOf<Post>() to CustomNavType.PostType
             )
         ) {
             val args = it.toRoute<PostNav>()
             PostScreen(
                 args.post, navController
             )
         }
         composable<WritingNav> {
             WritingNewPostScreen(navController)
         }
         composable<AddFridgeItemNav> {
             AddFridgeItemScreen(navController)
         }
         composable<ChattingRoomNav>(
             typeMap = mapOf(
                 typeOf<ChattingRoom>() to CustomNavType.ChatType
             )
         ) {
             val viewModel = it.sharedViewModel<ChatViewModel>(navController)
             val args = it.toRoute<ChattingRoomNav>()
             ChattingRoomScreen(viewModel.messageState, args.chattingRoom , navController)
         }
         composable<NotificationNav> {
             val viewModel = NotificationViewModel()
             NotificationScreen(navController, viewModel.state)
         }
     }

}

@Composable
inline fun <reified T: ViewModel> NavBackStackEntry.sharedViewModel(navController: NavHostController): T{
    val navGraphRoute = destination.parent?.route ?: return viewModel()
    val parentEntry = remember(this){
        navController.getBackStackEntry(navGraphRoute)
    }
    return viewModel(parentEntry)
}

object CustomNavType{
    val RecipeType = object: NavType<Recipe>(
        isNullableAllowed = false
    ){
        override fun get(bundle: Bundle, key: String): Recipe? {
            return Json.decodeFromString(bundle.getString(key) ?: return null)
        }

        override fun parseValue(value: String): Recipe {
            return Json.decodeFromString(Uri.decode(value))
        }

        override fun serializeAsValue(value: Recipe): String {
            return Uri.encode(Json.encodeToString(value))
        }

        override fun put(bundle: Bundle, key: String, value: Recipe) {
            bundle.putString(key, Json.encodeToString(value))
        }

    }
    val PostType = object: NavType<Post>(
        isNullableAllowed = false
    ){
        override fun get(bundle: Bundle, key: String): Post? {
            return Json.decodeFromString(bundle.getString(key) ?: return null)
        }

        override fun parseValue(value: String): Post {
            return Json.decodeFromString(Uri.decode(value))
        }

        override fun serializeAsValue(value: Post): String {
            return Uri.encode(Json.encodeToString(value))
        }

        override fun put(bundle: Bundle, key: String, value: Post) {
            bundle.putString(key, Json.encodeToString(value))
        }

    }
    val ChatType = object: NavType<ChattingRoom>(
        isNullableAllowed = false
    ){
        override fun get(bundle: Bundle, key: String): ChattingRoom? {
            return Json.decodeFromString(bundle.getString(key) ?: return null)
        }

        override fun parseValue(value: String): ChattingRoom {
            return Json.decodeFromString(Uri.decode(value))
        }

        override fun serializeAsValue(value: ChattingRoom): String {
            return Uri.encode(Json.encodeToString(value))
        }

        override fun put(bundle: Bundle, key: String, value: ChattingRoom) {
            bundle.putString(key, Json.encodeToString(value))
        }

    }
}
