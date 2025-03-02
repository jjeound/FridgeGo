package com.example.untitled_capstone.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.toRoute
import com.example.untitled_capstone.presentation.feature.notification.NotificationViewModel
import com.example.untitled_capstone.presentation.feature.chat.ChatViewModel
import com.example.untitled_capstone.presentation.feature.chat.screen.ChattingRoomScreen
import com.example.untitled_capstone.presentation.feature.chat.screen.ChattingScreen
import com.example.untitled_capstone.presentation.feature.home.screen.HomeScreen
import com.example.untitled_capstone.presentation.feature.home.HomeViewModel
import com.example.untitled_capstone.presentation.feature.home.screen.RecipeScreen
import com.example.untitled_capstone.presentation.feature.login.LoginScreen
import com.example.untitled_capstone.presentation.feature.main.MainViewModel
import com.example.untitled_capstone.presentation.feature.my.screen.MyScreen
import com.example.untitled_capstone.presentation.feature.notification.screen.NotificationScreen
import com.example.untitled_capstone.presentation.feature.refrigerator.FridgeViewModel
import com.example.untitled_capstone.presentation.feature.refrigerator.screen.AddFridgeItemScreen
import com.example.untitled_capstone.presentation.feature.refrigerator.screen.RefrigeratorScreen
import com.example.untitled_capstone.presentation.feature.shopping.PostViewModel
import com.example.untitled_capstone.presentation.feature.shopping.screen.PostScreen
import com.example.untitled_capstone.presentation.feature.shopping.screen.ShoppingScreen
import com.example.untitled_capstone.presentation.feature.shopping.screen.WritingNewPostScreen


@Composable
fun NavigationV2(navController: NavHostController, mainViewModel: MainViewModel) {
    NavHost(navController = navController, startDestination = Graph.HomeGraph){
        navigation<Graph.HomeGraph>(
            startDestination = Screen.Home
        ){
            composable<Screen.Home> {
                val viewModel = it.sharedViewModel<HomeViewModel>(navController)
                HomeScreen(viewModel.state, navController)
            }
            composable<Screen.RecipeNav>(
                typeMap = Screen.RecipeNav.typeMap
            ) {
                val args = it.toRoute<Screen.RecipeNav>()
                RecipeScreen(
                    args.recipe, navController
                )
            }
        }
        navigation<Graph.ShoppingGraph>(
            startDestination = Screen.Shopping
        ){
            composable<Screen.Shopping>{
                val viewModel = PostViewModel()
                ShoppingScreen(navController = navController, viewModel.state)
            }
            composable<Screen.PostNav>(
                typeMap = Screen.PostNav.typeMap
            ) {
                val args = it.toRoute<Screen.PostNav>()
                PostScreen(
                    args.post, navController
                )
            }
            composable<Screen.WritingNav> {
                WritingNewPostScreen(navController)
            }
        }
        navigation<Graph.FridgeGraph>(
            startDestination = Screen.Fridge
        ){
            composable<Screen.Fridge>{
                val viewModel = it.sharedViewModel<FridgeViewModel>(navController)
                RefrigeratorScreen(viewModel, mainViewModel)
            }
            composable<Screen.AddFridgeItemNav> {
                val viewModel = it.sharedViewModel<FridgeViewModel>(navController)
                AddFridgeItemScreen(navController, viewModel)
            }
        }
        navigation<Graph.ChatGraph>(
            startDestination = Screen.Chat
        ){
            composable<Screen.Chat>{
                val viewModel = it.sharedViewModel<ChatViewModel>(navController)
                ChattingScreen(viewModel.chatState, navController)
            }
            composable<Screen.ChattingRoomNav>(
                typeMap = Screen.ChattingRoomNav.typeMap
            ) {
                val viewModel = it.sharedViewModel<ChatViewModel>(navController)
                val args = it.toRoute<Screen.ChattingRoomNav>()
                ChattingRoomScreen(viewModel.messageState, args.chattingRoom , navController)
            }
        }
        navigation<Graph.MyGraph>(
            startDestination = Screen.My
        ){
            composable<Screen.My>{
                MyScreen(navController)
            }
        }
        composable<Screen.NotificationNav> {
            val viewModel = NotificationViewModel()
            NotificationScreen(navController, viewModel.state)
        }
        composable<Screen.LoginNav> {
            LoginScreen(navController)
        }
    }

}