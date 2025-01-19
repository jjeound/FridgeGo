package com.example.untitled_capstone

import android.annotation.SuppressLint
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import androidx.navigation.toRoute
import com.example.untitled_capstone.feature.chatting.Chatting
import com.example.untitled_capstone.feature.chatting.ChattingScreen
import com.example.untitled_capstone.feature.home.presentation.screen.Home
import com.example.untitled_capstone.feature.home.presentation.screen.HomeScreen
import com.example.untitled_capstone.feature.home.presentation.HomeViewModel
import com.example.untitled_capstone.feature.home.presentation.screen.RecipeNav
import com.example.untitled_capstone.feature.home.presentation.screen.RecipeScreen
import com.example.untitled_capstone.feature.my.My
import com.example.untitled_capstone.feature.my.MyScreen
import com.example.untitled_capstone.feature.refrigerator.Refrigerator
import com.example.untitled_capstone.feature.refrigerator.RefrigeratorScreen
import com.example.untitled_capstone.feature.shopping.Shopping
import com.example.untitled_capstone.feature.shopping.ShoppingScreen


@Composable
fun Navigation(navController: NavHostController) {
     NavHost(navController = navController, startDestination = Home){
         composable<Home>{
             val viewModel = HomeViewModel()
             HomeScreen(viewModel.state, navController)
         }
         composable<Shopping>{
             ShoppingScreen(navController = navController)
         }
         composable<Refrigerator>{
             RefrigeratorScreen()
         }
         composable<Chatting>{
             ChattingScreen()
         }
         composable<My>{
             MyScreen()
         }
//         navigation<Home>(startDestination = RecipeNav::class){
//             composable<RecipeNav> {
//                 val args = it.toRoute<RecipeNav>()
//                 RecipeScreen(recipe = args.recipe)
//             }
//         }
//         composable<ScreenA> {
//             Button(onClick = {
//                 navController.navigate(ScreenB(
//                     name = "John",
//                     age = 25
//                 ))
//             }){}
//         }
//         composable<ScreenB> {
//             val args = it.toRoute<ScreenB>()
//             Text(
//                 text = "${args.name}, ${args.age}"
//             )
//         }
     }

}

//@Serializable
//object ScreenA
//
//@Serializable
//data class ScreenB(
//    val name: String?,
//    val age: Int
//)