package com.example.untitled_capstone.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.toRoute
import com.example.untitled_capstone.data.remote.dto.KakaoLoginResponse
import com.example.untitled_capstone.presentation.feature.notification.NotificationViewModel
import com.example.untitled_capstone.presentation.feature.chat.ChatViewModel
import com.example.untitled_capstone.presentation.feature.chat.screen.ChattingRoomScreen
import com.example.untitled_capstone.presentation.feature.chat.screen.ChattingScreen
import com.example.untitled_capstone.presentation.feature.home.screen.HomeScreen
import com.example.untitled_capstone.presentation.feature.home.HomeViewModel
import com.example.untitled_capstone.presentation.feature.home.screen.RecipeScreen
import com.example.untitled_capstone.presentation.feature.login.KakaoLoginViewModel
import com.example.untitled_capstone.presentation.feature.login.screen.LoginScreen
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
                val state by viewModel.state.collectAsStateWithLifecycle()
                HomeScreen(state, viewModel::onAction) { id ->
                    navController.navigate(
                        Screen.RecipeNav(
                            id = id
                        )
                    )
                }
            }
            composable<Screen.RecipeNav>{
                val viewModel = it.sharedViewModel<HomeViewModel>(navController)
                val state by viewModel.state.collectAsStateWithLifecycle()
                val args = it.toRoute<Screen.RecipeNav>()
                RecipeScreen(
                    args.id, state, viewModel::onAction){
                    navController.popBackStack()
                }
            }
        }
        navigation<Graph.ShoppingGraph>(
            startDestination = Screen.Shopping
        ){
            composable<Screen.Shopping>{
                val viewModel = it.sharedViewModel<PostViewModel>(navController)
                val state by viewModel.state.collectAsStateWithLifecycle()
                ShoppingScreen(navigate = { id ->
                    navController.navigate(
                        Screen.PostNav(
                            id = id
                        )
                    )
                }, state = state, onAction = viewModel::onAction)
            }
            composable<Screen.PostNav>{
                val viewModel = it.sharedViewModel<PostViewModel>(navController)
                val state by viewModel.state.collectAsStateWithLifecycle()
                val args = it.toRoute<Screen.PostNav>()
                PostScreen(
                    args.id, state, viewModel::onAction, navController
                )
            }
            composable<Screen.WritingNav> {
                val viewModel = it.sharedViewModel<PostViewModel>(navController)
                WritingNewPostScreen(navController, viewModel)
            }
        }
        navigation<Graph.FridgeGraph>(
            startDestination = Screen.Fridge
        ){
            composable<Screen.Fridge>{
                val viewModel: FridgeViewModel = hiltViewModel(it)
                val state by viewModel.state
                RefrigeratorScreen(state, mainViewModel, viewModel::onAction, navController)
            }
            composable<Screen.AddFridgeItemNav>{
                val viewModel: FridgeViewModel = hiltViewModel(it)
                val state by viewModel.state
                val args = it.toRoute<Screen.AddFridgeItemNav>()
                AddFridgeItemScreen(args.id, state, {navController.popBackStack()}, viewModel::onAction)
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
            val viewModel: KakaoLoginViewModel = hiltViewModel()
            LoginScreen(navController, viewModel)
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