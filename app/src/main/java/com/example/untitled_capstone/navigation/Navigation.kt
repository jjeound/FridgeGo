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
import com.example.untitled_capstone.feature.chatting.ChattingScreen
import com.example.untitled_capstone.feature.home.domain.model.Recipe
import com.example.untitled_capstone.feature.home.presentation.screen.HomeScreen
import com.example.untitled_capstone.feature.home.presentation.HomeViewModel
import com.example.untitled_capstone.feature.home.presentation.screen.RecipeNav
import com.example.untitled_capstone.feature.home.presentation.screen.RecipeScreen
import com.example.untitled_capstone.feature.main.BottomScreen
import com.example.untitled_capstone.feature.my.MyScreen
import com.example.untitled_capstone.feature.refrigerator.RefrigeratorScreen
import com.example.untitled_capstone.feature.shopping.domain.model.Post
import com.example.untitled_capstone.feature.shopping.presentation.PostViewModel
import com.example.untitled_capstone.feature.shopping.presentation.screen.PostNav
import com.example.untitled_capstone.feature.shopping.presentation.screen.PostScreen
import com.example.untitled_capstone.feature.shopping.presentation.screen.ShoppingScreen
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
             RefrigeratorScreen()
         }
         composable(BottomScreen.Chat.route){
             ChattingScreen()
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
     }

}

object Graph{
    const val Main = "main_graph"
    const val Home = "home_graph"
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
}
