package com.example.untitled_capstone.navigation

import androidx.compose.material3.SnackbarHostState
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
import androidx.paging.compose.collectAsLazyPagingItems
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
import com.example.untitled_capstone.presentation.feature.login.SetLocationScreen
import com.example.untitled_capstone.presentation.feature.post.PostViewModel
import com.example.untitled_capstone.presentation.feature.post.screen.PostDetailScreen
import com.example.untitled_capstone.presentation.feature.post.screen.PostScreen
import com.example.untitled_capstone.presentation.feature.post.screen.WritingNewPostScreen


@Composable
fun NavigationV2(navController: NavHostController, mainViewModel: MainViewModel) {
    NavHost(navController = navController, startDestination = mainViewModel.startDestination.value){
        navigation<Graph.HomeGraph>(
            startDestination = Screen.Home
        ){
            composable<Screen.Home> {
                val viewModel: HomeViewModel = hiltViewModel()
                val recipeState by viewModel.recipeState.collectAsStateWithLifecycle()
                val recipeItems = viewModel.recipeItemsState.collectAsLazyPagingItems()
                val tastePrefState by viewModel.tastePrefState.collectAsStateWithLifecycle()
                val aiState by remember { viewModel.aiState }
                val snackBarHostState = remember { SnackbarHostState() }
                HomeScreen(snackBarHostState, mainViewModel, recipeState, recipeItems, tastePrefState, aiState, viewModel::onAction) { id ->
                    navController.navigate(
                        Screen.RecipeNav(
                            id = id
                        )
                    )
                }
            }
            composable<Screen.RecipeNav>{
                val viewModel: HomeViewModel = hiltViewModel()
                val recipeState by viewModel.recipeState.collectAsStateWithLifecycle()
                val args = it.toRoute<Screen.RecipeNav>()
                RecipeScreen(
                    args.id, recipeState, viewModel::onAction){
                    navController.popBackStack()
                }
            }
        }
        navigation<Graph.PostGraph>(
            startDestination = Screen.Post
        ){
            composable<Screen.Post>{
                val viewModel: PostViewModel = hiltViewModel()
                val state by viewModel.state.collectAsStateWithLifecycle()
                val postItems = viewModel.postItemState.collectAsLazyPagingItems()
                PostScreen(navigate = { id ->
                    navController.navigate(
                        Screen.PostDetailNav(
                            id = id
                        )
                    )
                }, postItems = postItems, state = state, onEvent = viewModel::onEvent)
            }
            composable<Screen.PostDetailNav>{
                val viewModel: PostViewModel = hiltViewModel()
                val state by viewModel.state.collectAsStateWithLifecycle()
                val args = it.toRoute<Screen.PostDetailNav>()
                PostDetailScreen(
                    args.id, state, viewModel::onEvent, navController
                )
            }
            composable<Screen.WritingNav> {
                val viewModel: PostViewModel = hiltViewModel()
                val state by viewModel.state.collectAsStateWithLifecycle()
                WritingNewPostScreen(navController, state, viewModel::onEvent)
            }
        }
        navigation<Graph.FridgeGraph>(
            startDestination = Screen.Fridge
        ){
            composable<Screen.Fridge>{
                val viewModel: FridgeViewModel = hiltViewModel()
                val state by viewModel.state.collectAsStateWithLifecycle()
                val fridgeItems = viewModel.fridgeItemState.collectAsLazyPagingItems()
                RefrigeratorScreen(fridgeItems, state, mainViewModel, viewModel::onAction, navController)
            }
            composable<Screen.AddFridgeItemNav>{
                val viewModel: FridgeViewModel = hiltViewModel()
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
                    navigateToLoc = {
                        navController.navigate(Screen.LocationNav)
                    },
                    popBackStack = {
                        navController.popBackStack()
                    },
                    onEvent = viewModel::onEvent,
                    state = state
                )
            }
            composable<Screen.LocationNav> {
                val viewModel: LoginViewModel = hiltViewModel()
                val state by viewModel.state.collectAsStateWithLifecycle()
                SetLocationScreen(state, viewModel::onEvent){
                    navController.navigate(Screen.Home)
                }
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