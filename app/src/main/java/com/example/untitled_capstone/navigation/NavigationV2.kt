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
import androidx.navigation.navigation
import androidx.navigation.toRoute
import com.example.untitled_capstone.presentation.feature.notification.NotificationViewModel
import com.example.untitled_capstone.presentation.feature.chat.ChatViewModel
import com.example.untitled_capstone.presentation.feature.chat.screen.ChattingRoomScreen
import com.example.untitled_capstone.presentation.feature.chat.screen.ChattingScreen
import com.example.untitled_capstone.presentation.feature.home.screen.HomeScreen
import com.example.untitled_capstone.presentation.feature.home.HomeViewModel
import com.example.untitled_capstone.presentation.feature.home.screen.RecipeScreen
import com.example.untitled_capstone.presentation.feature.login.LoginViewModel
import com.example.untitled_capstone.presentation.feature.login.LoginScreen
import com.example.untitled_capstone.presentation.feature.login.SetNickNameScreen
import com.example.untitled_capstone.presentation.feature.main.MainViewModel
import com.example.untitled_capstone.presentation.feature.my.MyScreen
import com.example.untitled_capstone.presentation.feature.my.MyViewModel
import com.example.untitled_capstone.presentation.feature.my.ProfileScreen
import com.example.untitled_capstone.presentation.feature.notification.screen.NotificationScreen
import com.example.untitled_capstone.presentation.feature.onBoardiing.OnBoarding
import com.example.untitled_capstone.presentation.feature.onBoardiing.OnBoardingViewModel
import com.example.untitled_capstone.presentation.feature.fridge.FridgeViewModel
import com.example.untitled_capstone.presentation.feature.fridge.screen.AddFridgeItemScreen
import com.example.untitled_capstone.presentation.feature.fridge.screen.RefrigeratorScreen
import com.example.untitled_capstone.presentation.feature.shopping.PostViewModel
import com.example.untitled_capstone.presentation.feature.shopping.screen.PostScreen
import com.example.untitled_capstone.presentation.feature.shopping.screen.ShoppingScreen
import com.example.untitled_capstone.presentation.feature.shopping.screen.WritingNewPostScreen


@Composable
fun NavigationV2(navController: NavHostController, mainViewModel: MainViewModel) {
    NavHost(navController = navController, startDestination = mainViewModel.startDestination.value){
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
                val state by viewModel.state.collectAsStateWithLifecycle()
                RefrigeratorScreen(state, mainViewModel, viewModel::onAction, navController)
            }
            composable<Screen.AddFridgeItemNav>{
                val viewModel: FridgeViewModel = hiltViewModel(it)
                val state by viewModel.state.collectAsStateWithLifecycle()
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
                val viewModel: MyViewModel = hiltViewModel()
                val state by viewModel.state.collectAsStateWithLifecycle()
                MyScreen(navController, viewModel::onEvent, state)
            }
            composable<Screen.Profile>{
                val viewModel: MyViewModel = hiltViewModel()
                val state by viewModel.state.collectAsStateWithLifecycle()
                ProfileScreen(state, {navController.popBackStack()})
            }
        }
        composable<Screen.NotificationNav> {
            val viewModel = NotificationViewModel()
            NotificationScreen(navController, viewModel.state)
        }
        navigation<Graph.LoginGraph>(startDestination = Screen.LoginNav){
            composable<Screen.LoginNav> {
                val viewModel: LoginViewModel = hiltViewModel()
                val state by viewModel.state.collectAsStateWithLifecycle()
                LoginScreen(navController, state, viewModel::onEvent)
            }
            composable<Screen.NicknameNav>{
                val viewModel: LoginViewModel = hiltViewModel()
                val state by viewModel.state.collectAsStateWithLifecycle()
                SetNickNameScreen(
                    navigateToHome = {
                        navController.navigate(Screen.Home)
                    },
                    popBackStack = {
                        navController.popBackStack()
                    },
                    onEvent = viewModel::onEvent,
                    state = state
                )
            }
        }
        navigation<Graph.OnBoardingGraph>(startDestination = Screen.OnBoarding) {
            composable<Screen.OnBoarding>{
                val viewModel: OnBoardingViewModel = hiltViewModel()
                OnBoarding(
                    navigateToHome = {
                        navController.navigate(Screen.Home)
                    },
                    navigateToLogin = {
                        navController.navigate(Screen.LoginNav)
                    },
                    onEvent = viewModel::onEvent
                )
            }
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