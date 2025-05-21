package com.example.untitled_capstone.navigation

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import androidx.navigation.toRoute
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.untitled_capstone.domain.model.Post
import com.example.untitled_capstone.presentation.feature.chat.ChatEvent
import com.example.untitled_capstone.presentation.feature.chat.ChatViewModel
import com.example.untitled_capstone.presentation.feature.chat.detail.ChattingDetailScreen
import com.example.untitled_capstone.presentation.feature.chat.detail.ChattingRoomDrawer
import com.example.untitled_capstone.presentation.feature.chat.ChattingScreen
import com.example.untitled_capstone.presentation.feature.chat.detail.ChatDetailViewModel
import com.example.untitled_capstone.presentation.feature.fridge.FridgeEvent
import com.example.untitled_capstone.presentation.feature.fridge.FridgeViewModel
import com.example.untitled_capstone.presentation.feature.fridge.crud.ScanExpirationDate
import com.example.untitled_capstone.presentation.feature.fridge.crud.AddFridgeItemScreen
import com.example.untitled_capstone.presentation.feature.fridge.RefrigeratorScreen
import com.example.untitled_capstone.presentation.feature.fridge.crud.FridgeCRUDViewModel
import com.example.untitled_capstone.presentation.feature.home.HomeScreen
import com.example.untitled_capstone.presentation.feature.home.HomeViewModel
import com.example.untitled_capstone.presentation.feature.home.detail.RecipeEvent
import com.example.untitled_capstone.presentation.feature.home.detail.RecipeScreen
import com.example.untitled_capstone.presentation.feature.home.detail.RecipeViewModel
import com.example.untitled_capstone.presentation.feature.home.modify.RecipeModifyScreen
import com.example.untitled_capstone.presentation.feature.home.modify.RecipeModifyViewModel
import com.example.untitled_capstone.presentation.feature.login.LoginViewModel
import com.example.untitled_capstone.presentation.feature.login.screen.LoginScreen
import com.example.untitled_capstone.presentation.feature.login.screen.SetLocationScreen
import com.example.untitled_capstone.presentation.feature.login.screen.SetNickNameScreen
import com.example.untitled_capstone.presentation.feature.main.AuthState
import com.example.untitled_capstone.presentation.feature.main.MainViewModel
import com.example.untitled_capstone.presentation.feature.my.MyViewModel
import com.example.untitled_capstone.presentation.feature.my.etc.MyLikedPostScreen
import com.example.untitled_capstone.presentation.feature.my.etc.MyPostScreen
import com.example.untitled_capstone.presentation.feature.my.MyScreen
import com.example.untitled_capstone.presentation.feature.my.profile.ProfileEvent
import com.example.untitled_capstone.presentation.feature.my.profile.ProfileScreen
import com.example.untitled_capstone.presentation.feature.my.profile.ProfileViewModel
import com.example.untitled_capstone.presentation.feature.notification.NotificationViewModel
import com.example.untitled_capstone.presentation.feature.notification.screen.NotificationScreen
import com.example.untitled_capstone.presentation.feature.onBoardiing.OnBoarding
import com.example.untitled_capstone.presentation.feature.post.PostEvent
import com.example.untitled_capstone.presentation.feature.post.detail.PostReportScreen
import com.example.untitled_capstone.presentation.feature.post.PostScreen
import com.example.untitled_capstone.presentation.feature.post.PostViewModel
import com.example.untitled_capstone.presentation.feature.post.crud.PostCRUDViewModel
import com.example.untitled_capstone.presentation.feature.post.crud.WritingNewPostScreen
import com.example.untitled_capstone.presentation.feature.post.detail.PostDetailScreen
import com.example.untitled_capstone.presentation.feature.post.detail.PostDetailViewModel
import com.example.untitled_capstone.presentation.feature.post.search.PostSearchScreen
import com.example.untitled_capstone.presentation.feature.post.search.PostSearchViewModel
import com.example.untitled_capstone.presentation.util.UiEvent


@Composable
fun Navigation(
    navController: NavHostController,
    mainViewModel: MainViewModel,
    snackbarHostState: SnackbarHostState
) {
    NavHost(navController = navController, startDestination = Graph.OnBoardingGraph){
        navigation<Graph.HomeGraph>(
            startDestination = Screen.Home
        ){
            composable<Screen.Home> {
                val viewModel: HomeViewModel = hiltViewModel()
                val uiState by viewModel.uiState.collectAsStateWithLifecycle()
                val aiResponse by viewModel.aiResponse.collectAsStateWithLifecycle()
                val tastePref by viewModel.tastePref.collectAsStateWithLifecycle()
                val recipeItems = viewModel.recipePagingData.collectAsLazyPagingItems()
                LaunchedEffect(true) {
                    viewModel.event.collect { event ->
                        when (event) {
                            is UiEvent.ShowSnackbar -> {
                                snackbarHostState.showSnackbar(event.message)
                            }
                            is UiEvent.Navigate -> {
                                navController.navigate(event.route)
                            }
                            is UiEvent.PopBackStack -> {
                                navController.popBackStack()
                            }
                        }
                    }
                }
                HomeScreen(
                    mainViewModel = mainViewModel,
                    uiState = uiState,
                    recipeItems = recipeItems,
                    aiResponse = aiResponse,
                    tastePref = tastePref,
                    onEvent = viewModel::onEvent,
                    onNavigate = { route ->
                        viewModel.navigateUp(route)
                    }
                )
            }
            composable<Screen.RecipeNav>{
                val viewModel: RecipeViewModel = hiltViewModel()
                val uiState by viewModel.uiState.collectAsStateWithLifecycle()
                val recipe by viewModel.recipe.collectAsStateWithLifecycle()
                val args = it.toRoute<Screen.RecipeNav>()
                LaunchedEffect(true) {
                    viewModel.event.collect { event ->
                        when (event) {
                            is RecipeEvent.ShowSnackbar -> {
                                snackbarHostState.showSnackbar(event.message)
                            }
                            is RecipeEvent.Navigate -> {
                                navController.navigate(event.route)
                            }
                            is RecipeEvent.PopBackStack -> {
                                navController.popBackStack()
                            }
                            is RecipeEvent.ClearBackStack -> {
                                navController.navigate(Graph.HomeGraph) {
                                    popUpTo(0) { inclusive = true } // 모든 백스택 제거
                                    launchSingleTop = true          // 중복 방지
                                }
                            }
                        }
                    }
                }
                LaunchedEffect(true) {
                    viewModel.getRecipeById(args.id)
                }
                RecipeScreen(
                    uiState = uiState,
                    recipe = recipe,
                    onNavigate = { route ->
                        viewModel.navigateUp(route)
                    },
                    popBackStack = { viewModel.popBackStack() },
                    deleteRecipe = viewModel::deleteRecipe,
                    toggleLike = viewModel::toggleLike
                )
            }
            composable<Screen.RecipeModifyNav>(
                typeMap = Screen.RecipeModifyNav.typeMap
            ){
                val viewModel: RecipeModifyViewModel = hiltViewModel()
                val uiState by viewModel.uiState.collectAsStateWithLifecycle()
                val args = it.toRoute<Screen.RecipeModifyNav>()
                LaunchedEffect(true) {
                    viewModel.event.collect { event ->
                        when (event) {
                            is UiEvent.ShowSnackbar -> {
                                snackbarHostState.showSnackbar(event.message)
                            }
                            is UiEvent.Navigate -> {
                                navController.navigate(event.route)
                            }
                            is UiEvent.PopBackStack -> {
                                navController.popBackStack()
                            }
                        }
                    }
                }
                RecipeModifyScreen(
                    uiState = uiState,
                    recipe = args.recipe,
                    uploadImageThenModifyRecipe = viewModel::uploadImageThenModify,
                    popBackStack = viewModel::popBackStack,
                )
            }
        }
        navigation<Graph.PostGraph>(
            startDestination = Screen.Post
        ){
            composable<Screen.Post>{
                val viewModel: PostViewModel = hiltViewModel()
                val postPagingData = viewModel.postPagingData.collectAsLazyPagingItems()
                LaunchedEffect(true) {
                    viewModel.fetchPosts()
                }
                LaunchedEffect(true) {
                    viewModel.event.collect { event ->
                        when (event) {
                            is PostEvent.ShowSnackbar -> {
                                snackbarHostState.showSnackbar(event.message)
                            }
                            is PostEvent.Navigate -> {
                                navController.navigate(event.route)
                            }
                            is PostEvent.PopBackStack -> {
                                navController.popBackStack()
                            }
                        }
                    }
                }
                PostScreen(
                    postPagingData = postPagingData,
                    navigateUp = viewModel::navigateUp,
                    toggleLike = viewModel::toggleLike
                )
            }
            composable<Screen.PostDetailNav>{
                val viewModel: PostDetailViewModel = hiltViewModel()
                val uiState by viewModel.uiState.collectAsStateWithLifecycle()
                val post by viewModel.post.collectAsStateWithLifecycle()
                val nickname by viewModel.nickname.collectAsStateWithLifecycle()
                val args = it.toRoute<Screen.PostDetailNav>()
                LaunchedEffect(true) {
                    viewModel.event.collect { event ->
                        when (event) {
                            is PostEvent.ShowSnackbar -> {
                                snackbarHostState.showSnackbar(event.message)
                            }
                            is PostEvent.Navigate -> {
                                navController.navigate(event.route)
                            }
                            is PostEvent.PopBackStack -> {
                                navController.popBackStack()
                            }
                            is PostEvent.ClearBackStack -> {
                                navController.navigate(Graph.PostGraph) {
                                    popUpTo(0) { inclusive = true } // 모든 백스택 제거
                                    launchSingleTop = true          // 중복 방지
                                }
                            }
                        }
                    }
                }
                PostDetailScreen(
                    id = args.id,
                    nickname = nickname,
                    uiState = uiState,
                    post = post,
                    getPostById = viewModel::getPostById,
                    toggleLike = viewModel::toggleLike,
                    navigateUp = {
                        navController.navigate(it)
                    },
                    deletePost = viewModel::deletePost,
                    clearBackStack = viewModel::clearBackStack,
                    savePost = { post ->
                        navController.currentBackStackEntry?.savedStateHandle["post"] = post
                    }
                )
            }
            composable<Screen.WritingNav>{
                val viewModel: PostCRUDViewModel = hiltViewModel()
                val uiState by viewModel.uiState.collectAsStateWithLifecycle()
                val post: Post? = navController.previousBackStackEntry?.savedStateHandle["post"]
                LaunchedEffect(true) {
                    viewModel.event.collect { event ->
                        when (event) {
                            is PostEvent.ShowSnackbar -> {
                                snackbarHostState.showSnackbar(event.message)
                            }
                            is PostEvent.Navigate -> {
                                navController.navigate(event.route)
                            }
                            is PostEvent.PopBackStack -> {
                                navController.popBackStack()
                            }
                            is PostEvent.ClearBackStack -> {
                                navController.navigate(Graph.PostGraph) {
                                    popUpTo(0) { inclusive = true } // 모든 백스택 제거
                                    launchSingleTop = true          // 중복 방지
                                }
                            }
                        }
                    }
                }
                WritingNewPostScreen(
                    uiState = uiState,
                    post = post,
                    deletePostImage = viewModel::deletePostImage,
                    modifyPost = viewModel::modifyPost,
                    addNewPost = viewModel::addNewPost,
                    popBackStack = viewModel::popBackStack,
                )
            }
            composable<Screen.PostSearchNav> {
                val viewModel: PostSearchViewModel = hiltViewModel()
                val uiState by viewModel.uiState.collectAsStateWithLifecycle()
                val searchPagingData = viewModel.searchPagingData.collectAsLazyPagingItems()
                val searchHistoryState by viewModel.keywords.collectAsStateWithLifecycle()
                LaunchedEffect(true) {
                    viewModel.getSearchHistory()
                }
                LaunchedEffect(true) {
                    viewModel.event.collect { event ->
                        when (event) {
                            is PostEvent.ShowSnackbar -> {
                                snackbarHostState.showSnackbar(event.message)
                            }
                            is PostEvent.Navigate -> {
                                navController.navigate(event.route)
                            }
                            is PostEvent.PopBackStack -> {
                                navController.popBackStack()
                            }
                            is PostEvent.ClearBackStack -> {
                                navController.navigate(Graph.PostGraph) {
                                    popUpTo(0) { inclusive = true } // 모든 백스택 제거
                                    launchSingleTop = true          // 중복 방지
                                }
                            }
                        }
                    }
                }
                PostSearchScreen(
                    uiState = uiState,
                    searchPagingData = searchPagingData,
                    searchHistoryState = searchHistoryState,
                    searchPost = viewModel::searchPost,
                    deleteSearchHistory = viewModel::deleteSearchHistory,
                    navigateUp = viewModel::navigateUp,
                    deleteAllSearchHistory = viewModel::deleteAllSearchHistory,
                    toggleLike = viewModel::toggleLike,
                    clearBackStack = viewModel::clearBackStack
                )
            }
            composable<Screen.ReportNav> {
                val parentEntry = navController.getBackStackEntry(Graph.PostGraph)
                val viewModel: PostDetailViewModel = hiltViewModel(parentEntry)
                val uiState by viewModel.uiState.collectAsStateWithLifecycle()
                val args = it.toRoute<Screen.ReportNav>()
                LaunchedEffect(true) {
                    viewModel.event.collect { event ->
                        when (event) {
                            is PostEvent.ShowSnackbar -> {
                                snackbarHostState.showSnackbar(event.message)
                            }
                            is PostEvent.Navigate -> {
                                navController.navigate(event.route)
                            }
                            is PostEvent.PopBackStack -> {
                                navController.popBackStack()
                            }
                        }
                    }
                }
                PostReportScreen(
                    uiState = uiState,
                    postId = args.id,
                    repostPost = if(args.isPost)viewModel::reportPost else viewModel::reportUser,
                    popBackStack = {navController.popBackStack()},
                )
            }
            composable<Screen.ChattingRoomNav>{
                val viewModel: ChatDetailViewModel = hiltViewModel()
                val uiState by viewModel.uiState.collectAsStateWithLifecycle()
                val messages = viewModel.message.collectAsLazyPagingItems()
                val members by viewModel.member.collectAsStateWithLifecycle()
                val chattingRoom by viewModel.chattingRoom.collectAsStateWithLifecycle()
                val chattingRoomList by viewModel.chattingRoomList.collectAsStateWithLifecycle()
                val name by viewModel.name.collectAsStateWithLifecycle()
                val args = it.toRoute<Screen.ChattingRoomNav>()
                LaunchedEffect(Unit) {
                    viewModel.enterChatRoom(args.id)
                    viewModel.getMessages(args.id)
                    viewModel.readChats(args.id)
                    viewModel.connectSocket(args.id)
                    viewModel.getMyName()
                    val isJoined = chattingRoomList.any{it.roomId == args.id}
                    if(!isJoined){
                        viewModel.joinChatRoom(args.id)
                    }
                }
                LaunchedEffect(messages.itemCount) {
                    viewModel.sendRead(args.id)
                }
                LaunchedEffect(true) {
                    viewModel.event.collect { event ->
                        when (event) {
                            is ChatEvent.ShowSnackbar -> {
                                snackbarHostState.showSnackbar(event.message)
                            }
                            is ChatEvent.Navigate -> {
                                navController.navigate(event.route)
                            }
                            is ChatEvent.PopBackStack -> {
                                navController.popBackStack()
                            }
                        }
                    }
                }
                ChattingDetailScreen(
                    messages = messages,
                    uiState = uiState,
                    roomId = args.id,
                    name = name,
                    members = members,
                    chattingRoom = chattingRoom,
                    clearBackStack = {
                        navController.navigate(Graph.ChatGraph) {
                            popUpTo(0) { inclusive = true } // 모든 백스택 제거
                            launchSingleTop = true          // 중복 방지
                        }
                     },
                    sendMessage = viewModel::sendMessage,
                    disconnect = viewModel::disconnect,
                    navigateUp = { route ->
                        navController.navigate(route)
                    }
                )
            }
            composable<Screen.ChattingDrawerNav>{
                val parentEntry = navController.getBackStackEntry(Graph.ChatGraph)
                val viewModel: ChatDetailViewModel = hiltViewModel(parentEntry)
                val uiState by viewModel.uiState.collectAsStateWithLifecycle()
                val members by viewModel.member.collectAsStateWithLifecycle()
                val myName by viewModel.name.collectAsStateWithLifecycle()
                val args = it.toRoute<Screen.ChattingDrawerNav>()
                LaunchedEffect(Unit) {
                    viewModel.checkWhoIsIn(args.id)
                    viewModel.getMyName()
                }
                LaunchedEffect(true) {
                    viewModel.event.collect { event ->
                        when (event) {
                            is ChatEvent.ShowSnackbar -> {
                                snackbarHostState.showSnackbar(event.message)
                            }
                            is ChatEvent.Navigate -> {
                                navController.navigate(event.route)
                            }
                            is ChatEvent.PopBackStack -> {
                                navController.popBackStack()
                            }
                        }
                    }
                }
                ChattingRoomDrawer(
                    uiState = uiState,
                    roomId = args.id,
                    title = args.title,
                    popBackStack = {navController.popBackStack()},
                    members = members,
                    myName = myName,
                    clearBackStack = {
                        navController.navigate(Graph.ChatGraph) {
                            popUpTo(0) { inclusive = true } // 모든 백스택 제거
                            launchSingleTop = true          // 중복 방지
                        }
                    },
                    closeChatRoom = viewModel::closeChatRoom,
                    exitChatRoom = viewModel::exitChatRoom
                )
            }
            composable<Screen.Profile>{
                val viewModel: ProfileViewModel = hiltViewModel()
                val uiState by viewModel.uiState.collectAsStateWithLifecycle()
                val profile by viewModel.profile.collectAsStateWithLifecycle()
                val args = it.toRoute<Screen.Profile>()
                LaunchedEffect(true) {
                    if (args.nickname != null) {
                        viewModel.getOtherProfile(args.nickname)
                    } else {
                        viewModel.getMyProfile()
                    }
                }
                ProfileScreen(
                    uiState = uiState,
                    isMe = args.nickname == null,
                    popBackStack = {navController.popBackStack()},
                    profile = profile,
                    navigateUp = {
                        navController.navigate(it)
                    },
                    logout = viewModel::logout,
                    uploadProfileImage = viewModel::uploadProfileImage,
                    clearBackStack = {
                        navController.navigate(Graph.LoginGraph) {
                            popUpTo(0) { inclusive = true } // 모든 백스택 제거
                            launchSingleTop = true          // 중복 방지
                        }
                    }
                )
            }
        }
        navigation<Graph.FridgeGraph>(
            startDestination = Screen.Fridge
        ){
            composable<Screen.Fridge>{
                val viewModel: FridgeViewModel = hiltViewModel()
                val uiState by viewModel.uiState.collectAsStateWithLifecycle()
                val fridgeItems = viewModel.fridgeItemPaged.collectAsLazyPagingItems()
                LaunchedEffect(true) {
                    viewModel.event.collect { event ->
                        when (event) {
                            is FridgeEvent.ShowSnackbar -> {
                                snackbarHostState.showSnackbar(event.message)
                            }
                            is FridgeEvent.Navigate -> {
                                navController.navigate(event.route)
                            }
                            is FridgeEvent.PopBackStack -> {
                                navController.popBackStack()
                            }
                        }
                    }
                }
                LaunchedEffect(true) {
                    viewModel.getItems()
                }
                RefrigeratorScreen(
                    fridgeItems = fridgeItems,
                    uiState = uiState,
                    topSelector = mainViewModel.topSelector,
                    navigateUp = { route ->
                        viewModel.navigateUp(route)
                    },
                    toggleNotification = viewModel::toggleNotification,
                    deleteItem = viewModel::deleteItem,
                    getItems = viewModel::getItems,
                    getItemsByDate = viewModel::getItemsByDate,
                )
            }
            composable<Screen.AddFridgeItemNav>{
                val viewModel: FridgeCRUDViewModel = hiltViewModel()
                val uiState by viewModel.uiState.collectAsStateWithLifecycle()
                val fridgeItem by viewModel.fridgeItem.collectAsStateWithLifecycle()
                val args = it.toRoute<Screen.AddFridgeItemNav>()
                LaunchedEffect(true) {
                    viewModel.event.collect { event ->
                        when (event) {
                            is FridgeEvent.ShowSnackbar -> {
                                snackbarHostState.showSnackbar(event.message)
                            }
                            is FridgeEvent.Navigate -> {
                                navController.navigate(event.route)
                            }
                            is FridgeEvent.PopBackStack -> {
                                navController.popBackStack()
                            }
                        }
                    }
                }
                LaunchedEffect(true) {
                    if(args.id != null){
                        viewModel.getItemById(args.id)
                    }
                }
                AddFridgeItemScreen(
                    fridgeItem = fridgeItem,
                    uiState = uiState,
                    initSavedDate = {
                        navController.currentBackStackEntry?.savedStateHandle?.remove<String>("date")
                    },
                    getSavedDate = {
                        navController.currentBackStackEntry?.savedStateHandle?.get<String>("date")
                    },
                    onNavigate = { route ->
                        viewModel.navigateUp(route)
                    },
                    popBackStack = {navController.popBackStack()},
                    showSnackbar = { message ->
                        viewModel.showSnackbar(message)
                    },
                    addFridgeItem = viewModel::addItem,
                    modifyFridgeItem = viewModel::modifyItem,
                )
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
                val viewModel: ChatViewModel = hiltViewModel()
                val uiState by viewModel.uiState.collectAsStateWithLifecycle()
                val chattingRoomList by viewModel.chattingRoomList.collectAsStateWithLifecycle()
                LaunchedEffect(true) {
                    viewModel.event.collect { event ->
                        when (event) {
                            is ChatEvent.ShowSnackbar -> {
                                snackbarHostState.showSnackbar(event.message)
                            }
                        }
                    }
                }
                ChattingScreen(
                    uiState = uiState,
                    chattingRoomList = chattingRoomList,
                    navigateUp = { route ->
                        navController.navigate(route)
                    }
                )
            }
            composable<Screen.ChattingRoomNav>{
                val viewModel: ChatDetailViewModel = hiltViewModel()
                val uiState by viewModel.uiState.collectAsStateWithLifecycle()
                val messages = viewModel.message.collectAsLazyPagingItems()
                val members by viewModel.member.collectAsStateWithLifecycle()
                val chattingRoom by viewModel.chattingRoom.collectAsStateWithLifecycle()
                val chattingRoomList by viewModel.chattingRoomList.collectAsStateWithLifecycle()
                val name by viewModel.name.collectAsStateWithLifecycle()
                val args = it.toRoute<Screen.ChattingRoomNav>()
                LaunchedEffect(Unit) {
                    viewModel.enterChatRoom(args.id)
                    viewModel.getMessages(args.id)
                    viewModel.readChats(args.id)
                    viewModel.connectSocket(args.id)
                    viewModel.getMyName()
                    val isJoined = chattingRoomList.any{it.roomId == args.id}
                    if(!isJoined){
                        viewModel.joinChatRoom(args.id)
                    }
                }
                LaunchedEffect(messages.itemCount) {
                    viewModel.sendRead(args.id)
                }
                LaunchedEffect(true) {
                    viewModel.event.collect { event ->
                        when (event) {
                            is ChatEvent.ShowSnackbar -> {
                                snackbarHostState.showSnackbar(event.message)
                            }
                            is ChatEvent.Navigate -> {
                                navController.navigate(event.route)
                            }
                            is ChatEvent.PopBackStack -> {
                                navController.popBackStack()
                            }
                        }
                    }
                }
                ChattingDetailScreen(
                    messages = messages,
                    uiState = uiState,
                    roomId = args.id,
                    name = name,
                    members = members,
                    chattingRoom = chattingRoom,
                    clearBackStack = {
                        navController.navigate(Graph.ChatGraph) {
                            popUpTo(0) { inclusive = true } // 모든 백스택 제거
                            launchSingleTop = true          // 중복 방지
                        }
                    },
                    sendMessage = viewModel::sendMessage,
                    disconnect = viewModel::disconnect,
                    navigateUp = { route ->
                        navController.navigate(route)
                    }
                )
            }
            composable<Screen.ChattingDrawerNav>{
                val parentEntry = navController.getBackStackEntry(Graph.ChatGraph)
                val viewModel: ChatDetailViewModel = hiltViewModel(parentEntry)
                val uiState by viewModel.uiState.collectAsStateWithLifecycle()
                val members by viewModel.member.collectAsStateWithLifecycle()
                val myName by viewModel.name.collectAsStateWithLifecycle()
                val args = it.toRoute<Screen.ChattingDrawerNav>()
                LaunchedEffect(Unit) {
                    viewModel.checkWhoIsIn(args.id)
                    viewModel.getMyName()
                }
                LaunchedEffect(true) {
                    viewModel.event.collect { event ->
                        when (event) {
                            is ChatEvent.ShowSnackbar -> {
                                snackbarHostState.showSnackbar(event.message)
                            }
                            is ChatEvent.Navigate -> {
                                navController.navigate(event.route){
                                    popUpTo(Screen.Chat){
                                        inclusive = true
                                    }
                                }
                            }
                            is ChatEvent.PopBackStack -> {
                                navController.popBackStack()
                            }
                        }
                    }
                }
                ChattingRoomDrawer(
                    uiState = uiState,
                    roomId = args.id,
                    title = args.title,
                    popBackStack = {navController.popBackStack()},
                    members = members,
                    myName = myName,
                    clearBackStack = {
                        navController.navigate(Graph.ChatGraph) {
                            popUpTo(0) { inclusive = true } // 모든 백스택 제거
                            launchSingleTop = true          // 중복 방지
                        }
                    },
                    closeChatRoom = viewModel::closeChatRoom,
                    exitChatRoom = viewModel::exitChatRoom
                )
            }
            composable<Screen.Profile>{
                val viewModel: ProfileViewModel = hiltViewModel()
                val uiState by viewModel.uiState.collectAsStateWithLifecycle()
                val profile by viewModel.profile.collectAsStateWithLifecycle()
                val args = it.toRoute<Screen.Profile>()
                LaunchedEffect(true) {
                    if (args.nickname != null) {
                        viewModel.getOtherProfile(args.nickname)
                    } else {
                        viewModel.getMyProfile()
                    }
                }
                LaunchedEffect(true) {
                    viewModel.event.collect { event ->
                        when (event) {
                            is ProfileEvent.ShowSnackbar -> {
                                snackbarHostState.showSnackbar(event.message)
                            }
                            is ProfileEvent.Navigate -> {
                                navController.navigate(event.route)
                            }
                            is ProfileEvent.PopBackStack -> {
                                navController.popBackStack()
                            }
                        }
                    }
                }
                ProfileScreen(
                    uiState = uiState,
                    isMe = args.nickname == null,
                    popBackStack = {navController.popBackStack()},
                    profile = profile,
                    navigateUp = {
                        navController.navigate(it)
                    },
                    logout = viewModel::logout,
                    uploadProfileImage = viewModel::uploadProfileImage,
                    clearBackStack = {
                        navController.navigate(Graph.LoginGraph) {
                            popUpTo(0) { inclusive = true } // 모든 백스택 제거
                            launchSingleTop = true          // 중복 방지
                        }
                    },
                )
            }
            composable<Screen.ReportNav> {
                val viewModel: PostDetailViewModel = hiltViewModel()
                val uiState by viewModel.uiState.collectAsStateWithLifecycle()
                val args = it.toRoute<Screen.ReportNav>()
                LaunchedEffect(true) {
                    viewModel.event.collect { event ->
                        when (event) {
                            is PostEvent.ShowSnackbar -> {
                                snackbarHostState.showSnackbar(event.message)
                            }
                            is PostEvent.Navigate -> {
                                navController.navigate(event.route)
                            }
                            is PostEvent.PopBackStack -> {
                                navController.popBackStack()
                            }
                        }
                    }
                }
                PostReportScreen(
                    uiState = uiState,
                    postId = args.id,
                    repostPost = if(args.isPost)viewModel::reportPost else viewModel::reportUser,
                    popBackStack = {navController.popBackStack()},
                )
            }
        }
        navigation<Graph.MyGraph>(
            startDestination = Screen.My
        ){
            composable<Screen.My>{
                val viewModel: MyViewModel = hiltViewModel()
                val profile by viewModel.profile.collectAsStateWithLifecycle()
                MyScreen(
                    profile = profile,
                    navigateUp = {
                        navController.navigate(it)
                    }
                )
            }
            composable<Screen.Profile>{
                val viewModel: ProfileViewModel = hiltViewModel()
                val uiState by viewModel.uiState.collectAsStateWithLifecycle()
                val profile by viewModel.profile.collectAsStateWithLifecycle()
                val args = it.toRoute<Screen.Profile>()
                LaunchedEffect(true) {
                    if (args.nickname != null) {
                        viewModel.getOtherProfile(args.nickname)
                    } else {
                        viewModel.getMyProfile()
                    }
                }
                LaunchedEffect(true) {
                    viewModel.event.collect { event ->
                        when (event) {
                            is ProfileEvent.ShowSnackbar -> {
                                snackbarHostState.showSnackbar(event.message)
                            }
                            is ProfileEvent.Navigate -> {
                                navController.navigate(event.route)
                            }
                            is ProfileEvent.PopBackStack -> {
                                navController.popBackStack()
                            }
                        }
                    }
                }
                ProfileScreen(
                    uiState = uiState,
                    isMe = args.nickname == null,
                    popBackStack = {navController.popBackStack()},
                    profile = profile,
                    navigateUp = {
                        navController.navigate(it)
                    },
                    logout = viewModel::logout,
                    uploadProfileImage = viewModel::uploadProfileImage,
                    clearBackStack = {
                        navController.navigate(Graph.LoginGraph) {
                            popUpTo(0) { inclusive = true } // 모든 백스택 제거
                            launchSingleTop = true          // 중복 방지
                        }
                    },
                )
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
                val postItems = viewModel.postPagingData.collectAsLazyPagingItems()
                LaunchedEffect(true) {
                    viewModel.getLikedPosts()
                }
                LaunchedEffect(true) {
                    viewModel.event.collect { event ->
                        when (event) {
                            is PostEvent.ShowSnackbar -> {
                                snackbarHostState.showSnackbar(event.message)
                            }
                            is PostEvent.Navigate -> {
                                navController.navigate(event.route)
                            }
                            is PostEvent.PopBackStack -> {
                                navController.popBackStack()
                            }
                        }
                    }
                }
                MyLikedPostScreen(
                    navigateUp = viewModel::navigateUp,
                    postItems = postItems,
                    popBackStack = viewModel::popBackStack,
                    toggleLike = viewModel::toggleLike,
                )
            }
            composable<Screen.MyPostNav> {
                val viewModel: PostViewModel = hiltViewModel()
                val postItems = viewModel.postPagingData.collectAsLazyPagingItems()
                LaunchedEffect(true) {
                    viewModel.getMyPosts()
                }
                LaunchedEffect(true) {
                    viewModel.event.collect { event ->
                        when (event) {
                            is PostEvent.ShowSnackbar -> {
                                snackbarHostState.showSnackbar(event.message)
                            }
                            is PostEvent.Navigate -> {
                                navController.navigate(event.route)
                            }
                            is PostEvent.PopBackStack -> {
                                navController.popBackStack()
                            }
                        }
                    }
                }
                MyPostScreen(
                    navigateUp = viewModel::navigateUp,
                    postItems = postItems,
                    popBackStack = viewModel::popBackStack,
                    toggleLike = viewModel::toggleLike,
                )
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
                        navController.navigate(Graph.HomeGraph)
                    },
                    from = false
                )
            }
        }
        navigation<Graph.OnBoardingGraph>(startDestination = Screen.OnBoarding) {
            composable<Screen.OnBoarding>{
                LaunchedEffect(true) {
                    mainViewModel.authEvent.collect { event ->
                        when (event) {
                            is AuthState.Login -> {
                                navController.navigate(route = Graph.HomeGraph)
                            }
                            is AuthState.Logout -> {
                                navController.navigate(route = Graph.LoginGraph)
                            }
                            is AuthState.Idle -> {}
                        }
                    }
                }
                OnBoarding(
                    navigateToLogin = {
                        navController.navigate(Screen.LoginNav)
                    },
                )
            }
        }
    }
}

