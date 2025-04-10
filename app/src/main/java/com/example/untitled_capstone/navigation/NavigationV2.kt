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
import com.example.untitled_capstone.presentation.feature.chat.screen.ChattingDetailScreen
import com.example.untitled_capstone.presentation.feature.chat.screen.ChattingRoomDrawer
import com.example.untitled_capstone.presentation.feature.chat.screen.ChattingScreen
import com.example.untitled_capstone.presentation.feature.home.screen.HomeScreen
import com.example.untitled_capstone.presentation.feature.home.HomeViewModel
import com.example.untitled_capstone.presentation.feature.home.screen.RecipeScreen
import com.example.untitled_capstone.presentation.feature.login.LoginViewModel
import com.example.untitled_capstone.presentation.feature.login.screen.LoginScreen
import com.example.untitled_capstone.presentation.feature.login.screen.SetNickNameScreen
import com.example.untitled_capstone.presentation.feature.main.MainViewModel
import com.example.untitled_capstone.presentation.feature.my.MyScreen
import com.example.untitled_capstone.presentation.feature.my.MyViewModel
import com.example.untitled_capstone.presentation.feature.my.ProfileScreen
import com.example.untitled_capstone.presentation.feature.notification.screen.NotificationScreen
import com.example.untitled_capstone.presentation.feature.onBoardiing.OnBoarding
import com.example.untitled_capstone.presentation.feature.fridge.FridgeViewModel
import com.example.untitled_capstone.presentation.feature.fridge.composable.ScanExpirationDate
import com.example.untitled_capstone.presentation.feature.fridge.screen.AddFridgeItemScreen
import com.example.untitled_capstone.presentation.feature.fridge.screen.RefrigeratorScreen
import com.example.untitled_capstone.presentation.feature.home.screen.RecipeModifyScreen
import com.example.untitled_capstone.presentation.feature.login.screen.SetLocationScreen
import com.example.untitled_capstone.presentation.feature.my.MyLikedPostScreen
import com.example.untitled_capstone.presentation.feature.my.MyPostScreen
import com.example.untitled_capstone.presentation.feature.post.PostEvent
import com.example.untitled_capstone.presentation.feature.post.PostViewModel
import com.example.untitled_capstone.presentation.feature.post.screen.PostDetailScreen
import com.example.untitled_capstone.presentation.feature.post.screen.PostScreen
import com.example.untitled_capstone.presentation.feature.post.screen.PostSearchScreen
import com.example.untitled_capstone.presentation.feature.post.screen.WritingNewPostScreen


@Composable
fun NavigationV2(navController: NavHostController, mainViewModel: MainViewModel) {
    NavHost(navController = navController, startDestination = mainViewModel.startDestination.value){
        navigation<Graph.HomeGraph>(
            startDestination = Screen.Home
        ){
            composable<Screen.Home> {
                val parentEntry = navController.getBackStackEntry(Graph.HomeGraph)
                val viewModel: HomeViewModel = hiltViewModel(parentEntry)
                val recipeState by viewModel.recipeState.collectAsStateWithLifecycle()
                val recipeItems = viewModel.recipeItemsState.collectAsLazyPagingItems()
                val tastePrefState by viewModel.tastePrefState.collectAsStateWithLifecycle()
                val aiState by remember { viewModel.aiState }
                HomeScreen(mainViewModel, recipeState, recipeItems, tastePrefState, aiState, viewModel::onEvent) { id ->
                    navController.navigate(
                        Screen.RecipeNav(
                            id = id
                        )
                    )
                }
            }
            composable<Screen.RecipeNav>{
                val parentEntry = navController.getBackStackEntry(Graph.HomeGraph)
                val viewModel: HomeViewModel = hiltViewModel(parentEntry)
                val recipeState by viewModel.recipeState.collectAsStateWithLifecycle()
                val args = it.toRoute<Screen.RecipeNav>()
                RecipeScreen(args.id, recipeState, viewModel::onEvent, navController)
            }
            composable<Screen.RecipeModifyNav>{
                val parentEntry = navController.getBackStackEntry(Graph.HomeGraph)
                val viewModel: HomeViewModel = hiltViewModel(parentEntry)
                val recipeState by viewModel.recipeState.collectAsStateWithLifecycle()
                val modifyState by viewModel.modifyState.collectAsStateWithLifecycle()
                RecipeModifyScreen(
                    recipe = recipeState.recipe!!,
                    state = modifyState,
                    onEvent = viewModel::onEvent,
                    navigateToBack = {navController.popBackStack()}
                )
            }
        }
        navigation<Graph.PostGraph>(
            startDestination = Screen.Post
        ){
            composable<Screen.Post>{
                val parentEntry = navController.getBackStackEntry(Graph.PostGraph)
                val viewModel: PostViewModel = hiltViewModel(parentEntry)
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
                val parentEntry = navController.getBackStackEntry(Graph.PostGraph)
                val viewModel: PostViewModel = hiltViewModel(parentEntry)
                val state by viewModel.state.collectAsStateWithLifecycle()
                val args = it.toRoute<Screen.PostDetailNav>()
                PostDetailScreen(
                    args.id,
                    viewModel.nickname,
                    state, viewModel::onEvent, navController
                )
            }
            composable<Screen.WritingNav> {
                val parentEntry = navController.getBackStackEntry(Graph.PostGraph)
                val viewModel: PostViewModel = hiltViewModel(parentEntry)
                val state by viewModel.state.collectAsStateWithLifecycle()
                val uploadState by viewModel.uploadState.collectAsStateWithLifecycle()
                WritingNewPostScreen(navController, state, uploadState, viewModel::onEvent)
            }
            composable<Screen.PostSearchNav> {
                val viewModel: PostViewModel = hiltViewModel()
                val searchState by viewModel.searchState
                val searchHistory by remember { viewModel.keyword }
                PostSearchScreen(
                    searchState = searchState,
                    searchHistory = searchHistory,
                    navigateToBack = {navController.popBackStack()},
                    onEvent = viewModel::onEvent,
                    navigateToDetail = { id ->
                        navController.navigate(
                            Screen.PostDetailNav(
                                id = id
                            )
                        )
                    }
                )
            }
        }
        navigation<Graph.FridgeGraph>(
            startDestination = Screen.Fridge
        ){
            composable<Screen.Fridge>{
                val parentEntry = navController.getBackStackEntry(Graph.FridgeGraph)
                val viewModel: FridgeViewModel = hiltViewModel(parentEntry)
                val state by viewModel.state.collectAsStateWithLifecycle()
                val fridgeItems = viewModel.fridgeItemState.collectAsLazyPagingItems()
                RefrigeratorScreen(fridgeItems, state, mainViewModel, viewModel::onAction, navController)
            }
            composable<Screen.AddFridgeItemNav>{
                val parentEntry = navController.getBackStackEntry(Graph.FridgeGraph)
                val viewModel: FridgeViewModel = hiltViewModel(parentEntry)
                val state by viewModel.state.collectAsStateWithLifecycle()
                val args = it.toRoute<Screen.AddFridgeItemNav>()
                AddFridgeItemScreen(args.id, state, navController, viewModel::onAction)
            }
            composable<Screen.ScanNav> {
                ScanExpirationDate(
                    navController
                )
            }
        }
        navigation<Graph.ChatGraph>(
            startDestination = Screen.Chat
        ){
            composable<Screen.Chat>{
                val parentEntry = navController.getBackStackEntry(Graph.ChatGraph)
                val viewModel: ChatViewModel = hiltViewModel(parentEntry)
                val state by viewModel.state.collectAsStateWithLifecycle()
                ChattingScreen(
                    snackbarHostState = remember { SnackbarHostState() },
                    viewModel = viewModel,
                    state = state,
                    navController = navController,
                )
            }
            composable<Screen.ChattingRoomNav>{
                val parentEntry = navController.getBackStackEntry(Graph.ChatGraph)
                val viewModel: ChatViewModel = hiltViewModel(parentEntry)
                val state by viewModel.state.collectAsStateWithLifecycle()
                val messages = viewModel.message.collectAsLazyPagingItems()
                val args = it.toRoute<Screen.ChattingRoomNav>()
                ChattingDetailScreen(
                    snackbarHostState = remember { SnackbarHostState() },
                    viewModel = viewModel,
                    messages = messages,
                    state = state,
                    roomId = args.id,
                    navController = navController
                )
            }
            composable<Screen.ChattingDrawerNav>{
                val parentEntry = navController.getBackStackEntry(Graph.ChatGraph)
                val viewModel: ChatViewModel = hiltViewModel(parentEntry)
                val state by viewModel.state.collectAsStateWithLifecycle()
                val args = it.toRoute<Screen.ChattingDrawerNav>()
                ChattingRoomDrawer(
                    snackbarHostState = remember { SnackbarHostState() },
                    viewModel = viewModel,
                    state = state,
                    roomId = args.id,
                    title = args.title,
                    navController = navController
                )
            }
        }
        navigation<Graph.MyGraph>(
            startDestination = Screen.My
        ){
            composable<Screen.My>{
                val parentEntry = navController.getBackStackEntry(Graph.MyGraph)
                val viewModel: MyViewModel = hiltViewModel(parentEntry)
                val state by viewModel.state.collectAsStateWithLifecycle()
                MyScreen(navController, viewModel::onEvent, state)
            }
            composable<Screen.Profile>{
                val parentEntry = navController.getBackStackEntry(Graph.MyGraph)
                val viewModel: MyViewModel = hiltViewModel(parentEntry)
                val state by viewModel.state.collectAsStateWithLifecycle()
                ProfileScreen(navController, state, viewModel::onEvent, {navController.popBackStack()})
            }
            composable<Screen.NicknameNav>{
                val viewModel: LoginViewModel = hiltViewModel()
                val state by viewModel.validateState.collectAsStateWithLifecycle()
                SetNickNameScreen(
                    navigateToLoc = {
                        navController.navigate(Screen.LocationNav)
                    },
                    popBackStack = {
                        navController.popBackStack()
                    },
                    onEvent = viewModel::onEvent,
                    state = state,
                    from = true
                )
            }
            composable<Screen.LocationNav> {
                val viewModel: LoginViewModel = hiltViewModel()
                val state by viewModel.addressState.collectAsStateWithLifecycle()
                SetLocationScreen(
                    state = state,
                    onEvent = viewModel::onEvent,
                    popBackStack = {navController.popBackStack()},
                    navigateToHome = {navController.navigate(Screen.Home)},
                    from = true
                )
            }
            composable<Screen.MyLikedPostNav> {
                val viewModel: PostViewModel = hiltViewModel()
                val postItems = viewModel.postItemState.collectAsLazyPagingItems()
                viewModel.onEvent(PostEvent.GetLikedPosts)
                MyLikedPostScreen(navigate = { id ->
                    navController.navigate(
                        Screen.PostDetailNav(
                            id = id
                        )
                    )
                }, postItems = postItems,onEvent = viewModel::onEvent,
                    navigateToBack = {navController.popBackStack()})
            }
            composable<Screen.MyPostNav> {
                val viewModel: PostViewModel = hiltViewModel()
                val postItems = viewModel.postItemState.collectAsLazyPagingItems()
                viewModel.onEvent(PostEvent.GetMyPosts)
                MyPostScreen(navigate = { id ->
                    navController.navigate(
                        Screen.PostDetailNav(
                            id = id
                        )
                    )
                }, postItems = postItems, onEvent = viewModel::onEvent,
                    navigateToBack = {navController.popBackStack()})
            }
        }
        composable<Screen.NotificationNav> {
            val viewModel = NotificationViewModel()
            NotificationScreen(navController, viewModel.state)
        }
        navigation<Graph.LoginGraph>(startDestination = Screen.LoginNav){
            composable<Screen.LoginNav> {
                val parentEntry = navController.getBackStackEntry(Graph.LoginGraph)
                val viewModel: LoginViewModel = hiltViewModel(parentEntry)
                val state by viewModel.state.collectAsStateWithLifecycle()
                LoginScreen(navController, state, viewModel::onEvent)
            }
            composable<Screen.NicknameNav>{
                val parentEntry = navController.getBackStackEntry(Graph.LoginGraph)
                val viewModel: LoginViewModel = hiltViewModel(parentEntry)
                val state by viewModel.validateState.collectAsStateWithLifecycle()
                SetNickNameScreen(
                    navigateToLoc = {
                        navController.navigate(Screen.LocationNav)
                    },
                    popBackStack = {
                        navController.popBackStack()
                    },
                    onEvent = viewModel::onEvent,
                    state = state,
                    from = false
                )
            }
            composable<Screen.LocationNav> {
                val parentEntry = navController.getBackStackEntry(Graph.LoginGraph)
                val viewModel: LoginViewModel = hiltViewModel(parentEntry)
                val state by viewModel.addressState.collectAsStateWithLifecycle()
                SetLocationScreen(
                    state = state,
                    onEvent = viewModel::onEvent,
                    popBackStack = {navController.popBackStack()},
                    navigateToHome = {
                        navController.navigate(Graph.HomeGraph) {
                            popUpTo(0) { inclusive = true }
                        }
                    },
                    from = false
                )
            }
        }
        navigation<Graph.OnBoardingGraph>(startDestination = Screen.OnBoarding) {
            composable<Screen.OnBoarding>{
                OnBoarding(
                    navigateToLogin = {
                        navController.navigate(Screen.LoginNav)
                    },
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