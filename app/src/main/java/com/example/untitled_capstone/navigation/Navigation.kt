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
import com.example.untitled_capstone.presentation.feature.chat.ChatViewModel
import com.example.untitled_capstone.presentation.feature.chat.detail.ChattingDetailScreen
import com.example.untitled_capstone.presentation.feature.chat.detail.ChattingRoomDrawer
import com.example.untitled_capstone.presentation.feature.chat.ChattingScreen
import com.example.untitled_capstone.presentation.feature.chat.detail.ChatDetailViewModel
import com.example.untitled_capstone.presentation.feature.fridge.FridgeViewModel
import com.example.untitled_capstone.presentation.feature.fridge.crud.ScanExpirationDate
import com.example.untitled_capstone.presentation.feature.fridge.crud.AddFridgeItemScreen
import com.example.untitled_capstone.presentation.feature.fridge.RefrigeratorScreen
import com.example.untitled_capstone.presentation.feature.fridge.crud.FridgeCRUDViewModel
import com.example.untitled_capstone.presentation.feature.home.HomeScreen
import com.example.untitled_capstone.presentation.feature.home.HomeViewModel
import com.example.untitled_capstone.presentation.feature.home.detail.RecipeScreen
import com.example.untitled_capstone.presentation.feature.home.detail.RecipeViewModel
import com.example.untitled_capstone.presentation.feature.home.detail.RecipeModifyScreen
import com.example.untitled_capstone.presentation.feature.login.LoginViewModel
import com.example.untitled_capstone.presentation.feature.login.LoginScreen
import com.example.untitled_capstone.presentation.feature.login.SetLocationScreen
import com.example.untitled_capstone.presentation.feature.login.SetNickNameScreen
import com.example.untitled_capstone.presentation.feature.main.MainViewModel
import com.example.untitled_capstone.presentation.feature.my.MyViewModel
import com.example.untitled_capstone.presentation.feature.my.etc.MyLikedPostScreen
import com.example.untitled_capstone.presentation.feature.my.etc.MyPostScreen
import com.example.untitled_capstone.presentation.feature.my.MyScreen
import com.example.untitled_capstone.presentation.feature.my.profile.ProfileModifyScreen
import com.example.untitled_capstone.presentation.feature.my.profile.ProfileScreen
import com.example.untitled_capstone.presentation.feature.my.profile.ProfileViewModel
import com.example.untitled_capstone.presentation.feature.notification.NotificationViewModel
import com.example.untitled_capstone.presentation.feature.notification.NotificationScreen
import com.example.untitled_capstone.presentation.feature.onBoardiing.OnBoarding
import com.example.untitled_capstone.presentation.feature.post.detail.ReportScreen
import com.example.untitled_capstone.presentation.feature.post.PostScreen
import com.example.untitled_capstone.presentation.feature.post.PostViewModel
import com.example.untitled_capstone.presentation.feature.post.crud.PostCRUDViewModel
import com.example.untitled_capstone.presentation.feature.post.crud.WritingNewPostScreen
import com.example.untitled_capstone.presentation.feature.post.detail.PostDetailScreen
import com.example.untitled_capstone.presentation.feature.post.detail.PostDetailViewModel
import com.example.untitled_capstone.presentation.feature.post.detail.PostProfileScreen
import com.example.untitled_capstone.presentation.feature.post.search.PostSearchScreen
import com.example.untitled_capstone.presentation.feature.post.search.PostSearchViewModel
import com.example.untitled_capstone.presentation.util.AuthEvent
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
                    navigate = { route ->
                        navController.navigate(route)
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
                            is UiEvent.ShowSnackbar -> {
                                snackbarHostState.showSnackbar(event.message)
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
                    navigate = { route ->
                        navController.navigate(route)
                    },
                    popBackStack = { navController.popBackStack()},
                    deleteRecipe = viewModel::deleteRecipe,
                    toggleLike = viewModel::toggleLike,
                    clearBackStack = {
                        navController.navigate(Graph.HomeGraph) {
                            popUpTo(0) { inclusive = true } // 모든 백스택 제거
                            launchSingleTop = true          // 중복 방지
                        }
                    }
                )
            }
            composable<Screen.RecipeModifyNav>{
                val viewModel: RecipeViewModel = hiltViewModel()
                val uiState by viewModel.uiState.collectAsStateWithLifecycle()
                val recipe by viewModel.recipe.collectAsStateWithLifecycle()
                LaunchedEffect(true) {
                    viewModel.event.collect { event ->
                        when (event) {
                            is UiEvent.ShowSnackbar -> {
                                snackbarHostState.showSnackbar(event.message)
                            }
                        }
                    }
                }
                val args = it.toRoute<Screen.RecipeModifyNav>()
                LaunchedEffect(true) {
                    viewModel.getRecipeById(args.id)
                }
                RecipeModifyScreen(
                    uiState = uiState,
                    recipe = recipe,
                    uploadImageThenModifyRecipe = viewModel::uploadImageThenModify,
                    popBackStack = {navController.popBackStack()},
                    clearBackStack = {
                        navController.navigate(Graph.HomeGraph) {
                            popUpTo(0) { inclusive = true } // 모든 백스택 제거
                            launchSingleTop = true          // 중복 방지
                        }
                    }
                )
            }
        }
        navigation<Graph.PostGraph>(
            startDestination = Screen.Post
        ){
            composable<Screen.Post>{
                val viewModel: PostViewModel = hiltViewModel()
                val postPagingData = viewModel.postPagingData.collectAsLazyPagingItems()
                val uiState by viewModel.uiState.collectAsStateWithLifecycle()
                LaunchedEffect(true) {
                    viewModel.event.collect { event ->
                        when (event) {
                            is UiEvent.ShowSnackbar -> {
                                snackbarHostState.showSnackbar(event.message)
                            }
                        }
                    }
                }
                PostScreen(
                    uiState = uiState,
                    postPagingData = postPagingData,
                    navigate = { route ->
                        navController.navigate(route)
                    },
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
                            is UiEvent.ShowSnackbar -> {
                                snackbarHostState.showSnackbar(event.message)
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
                    navigate = { route ->
                        navController.navigate(route)
                    },
                    deletePost = viewModel::deletePost,
                    clearBackStack = {
                        navController.navigate(Graph.PostGraph) {
                            popUpTo(0) { inclusive = true } // 모든 백스택 제거
                            launchSingleTop = true          // 중복 방지
                        }
                    },
                    savePost = { post ->
                        navController.currentBackStackEntry?.savedStateHandle["post"] = post
                    },
                )
            }
            composable<Screen.WritingNav>{
                val viewModel: PostCRUDViewModel = hiltViewModel()
                val uiState by viewModel.uiState.collectAsStateWithLifecycle()
                val post: Post? = navController.previousBackStackEntry?.savedStateHandle["post"]
                LaunchedEffect(true) {
                    viewModel.event.collect { event ->
                        when (event) {
                            is UiEvent.ShowSnackbar -> {
                                snackbarHostState.showSnackbar(event.message)
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
                    popBackStack = {navController.popBackStack()},
                    clearBackStack = {
                        navController.navigate(Graph.PostGraph) {
                            popUpTo(0) { inclusive = true } // 모든 백스택 제거
                            launchSingleTop = true          // 중복 방지
                        }
                    },
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
                            is UiEvent.ShowSnackbar -> {
                                snackbarHostState.showSnackbar(event.message)
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
                    navigate = { route ->
                        navController.navigate(route)
                    },
                    deleteAllSearchHistory = viewModel::deleteAllSearchHistory,
                    toggleLike = viewModel::toggleLike,
                    clearBackStack = {
                        navController.navigate(Graph.PostGraph) {
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
                            is UiEvent.ShowSnackbar -> {
                                snackbarHostState.showSnackbar(event.message)
                            }
                        }
                    }
                }
                ReportScreen(
                    uiState = uiState,
                    id = args.id,
                    reportPost = viewModel::reportPost,
                    reportUser = viewModel::reportUser,
                    popBackStack = {navController.popBackStack()},
                    isPost = args.isPost,
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
                    viewModel.fcmEnterRoom(args.id)
                    viewModel.getMessages(args.id)
                    viewModel.connectSocket(args.id)
                    viewModel.getMyName()
                    val isJoined = chattingRoomList.any{it.roomId == args.id}
                    if(!isJoined){
                        viewModel.joinChatRoom(args.id)
                    }else{
                        viewModel.readChats(args.id)
                    }
                }
                LaunchedEffect(messages.itemCount) {
                    viewModel.sendRead(args.id)
                }
                LaunchedEffect(true) {
                    viewModel.event.collect { event ->
                        when (event) {
                            is UiEvent.ShowSnackbar -> {
                                snackbarHostState.showSnackbar(event.message)
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
                        viewModel.fcmLeaveRoom(args.id)
                        navController.navigate(Graph.ChatGraph) {
                            popUpTo(0) { inclusive = true } // 모든 백스택 제거
                            launchSingleTop = true          // 중복 방지
                        }
                    },
                    sendMessage = viewModel::sendMessage,
                    disconnect = viewModel::disconnect,
                    navigate = { route ->
                        navController.navigate(route)
                    },
                    leaveRoom = viewModel::fcmLeaveRoom,
                )
            }
            composable<Screen.ChattingDrawerNav>{
                val viewModel: ChatDetailViewModel = hiltViewModel()
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
                            is UiEvent.ShowSnackbar -> {
                                snackbarHostState.showSnackbar(event.message)
                            }
                        }
                    }
                }
                ChattingRoomDrawer(
                    uiState = uiState,
                    roomId = args.id,
                    title = args.title,
                    isActive = args.isActive,
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
                    exitChatRoom = viewModel::exitChatRoom,
                    navigate = {
                        navController.navigate(it)
                    },
                )
            }
            composable<Screen.PostProfileNav>{
                val viewModel: ProfileViewModel = hiltViewModel()
                val uiState by viewModel.uiState.collectAsStateWithLifecycle()
                val profile by viewModel.profile.collectAsStateWithLifecycle()
                val args = it.toRoute<Screen.PostProfileNav>()
                LaunchedEffect(true) {
                    if (args.nickname != null) {
                        viewModel.getOtherProfile(args.nickname)
                    } else {
                        viewModel.getMyProfile()
                    }
                }
                PostProfileScreen(
                    uiState = uiState,
                    isMe = args.nickname == null,
                    popBackStack = {navController.popBackStack()},
                    profile = profile,
                    navigate = {
                        navController.navigate(it)
                    },
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
                            is UiEvent.ShowSnackbar -> {
                                snackbarHostState.showSnackbar(event.message)
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
                    navigate = { route ->
                        navController.navigate(route)
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
                            is UiEvent.ShowSnackbar -> {
                                snackbarHostState.showSnackbar(event.message)
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
                    isFridge = args.isFridge,
                    initSavedDate = {
                        navController.currentBackStackEntry?.savedStateHandle?.remove<String>("date")
                    },
                    getSavedDate = {
                        navController.currentBackStackEntry?.savedStateHandle?.get<String>("date")
                    },
                    navigate = { route ->
                        navController.navigate(route)
                    },
                    popBackStack = {navController.popBackStack()},
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
                            is UiEvent.ShowSnackbar -> {
                                snackbarHostState.showSnackbar(event.message)
                            }
                        }
                    }
                }
                ChattingScreen(
                    uiState = uiState,
                    chattingRoomList = chattingRoomList,
                    navigate = { route ->
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
                    viewModel.fcmEnterRoom(args.id)
                    viewModel.connectSocket(args.id)
                    viewModel.checkWhoIsIn(args.id)
                    viewModel.getMessages(args.id)
                    viewModel.getMyName()
                }
                if(args.isActive){
                    LaunchedEffect(Unit) {
                        val isJoined = chattingRoomList.any{it.roomId == args.id}
                        if(!isJoined){
                            viewModel.joinChatRoom(args.id)
                        }else {
                            viewModel.readChats(args.id)
                        }
                    }
                    LaunchedEffect(messages.itemCount) {
                        viewModel.sendRead(args.id)
                    }
                }
                LaunchedEffect(true) {
                    viewModel.event.collect { event ->
                        when (event) {
                            is UiEvent.ShowSnackbar -> {
                                snackbarHostState.showSnackbar(event.message)
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
                        viewModel.fcmLeaveRoom(args.id)
                        navController.navigate(Graph.ChatGraph) {
                            popUpTo(0) { inclusive = true } // 모든 백스택 제거
                            launchSingleTop = true          // 중복 방지
                        }
                    },
                    sendMessage = viewModel::sendMessage,
                    disconnect = viewModel::disconnect,
                    navigate = { route ->
                        navController.navigate(route)
                    },
                    leaveRoom = viewModel::fcmLeaveRoom,
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
                            is UiEvent.ShowSnackbar -> {
                                snackbarHostState.showSnackbar(event.message)
                            }
                        }
                    }
                }
                ChattingRoomDrawer(
                    uiState = uiState,
                    roomId = args.id,
                    title = args.title,
                    isActive = args.isActive,
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
                    exitChatRoom = viewModel::exitChatRoom,
                    navigate = {
                        navController.navigate(it)
                    },
                )
            }
            composable<Screen.PostProfileNav>{
                val viewModel: ProfileViewModel = hiltViewModel()
                val uiState by viewModel.uiState.collectAsStateWithLifecycle()
                val profile by viewModel.profile.collectAsStateWithLifecycle()
                val args = it.toRoute<Screen.PostProfileNav>()
                LaunchedEffect(true) {
                    if (args.nickname != null) {
                        viewModel.getOtherProfile(args.nickname)
                    } else {
                        viewModel.getMyProfile()
                    }
                }
                PostProfileScreen(
                    uiState = uiState,
                    isMe = args.nickname == null,
                    popBackStack = {navController.popBackStack()},
                    profile = profile,
                    navigate = {
                        navController.navigate(it)
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
                            is UiEvent.ShowSnackbar -> {
                                snackbarHostState.showSnackbar(event.message)
                            }
                        }
                    }
                }
                ReportScreen(
                    uiState = uiState,
                    id = args.id,
                    reportPost = viewModel::reportPost,
                    reportUser = viewModel::reportUser,
                    popBackStack = {navController.popBackStack()},
                    isPost = false,
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
                    navigate = {
                        navController.navigate(it)
                    }
                )
            }
            composable<Screen.Profile>{
                val viewModel: ProfileViewModel = hiltViewModel()
                val uiState by viewModel.uiState.collectAsStateWithLifecycle()
                val profile by viewModel.profile.collectAsStateWithLifecycle()
                LaunchedEffect(true) {
                    viewModel.getMyProfile()
                }
                LaunchedEffect(true) {
                    viewModel.event.collect { event ->
                        when (event) {
                            is UiEvent.ShowSnackbar -> {
                                snackbarHostState.showSnackbar(event.message)
                            }
                        }
                    }
                }
                ProfileScreen(
                    uiState = uiState,
                    popBackStack = {navController.popBackStack()},
                    profile = profile,
                    navigate = {
                        navController.navigate(it)
                    },
                    logout = viewModel::logout,
                    goToLoginScreen = {
                        navController.navigate(Graph.LoginGraph) {
                            popUpTo(0) { inclusive = true } // 모든 백스택 제거
                            launchSingleTop = true          // 중복 방지
                        }
                    },
                    clearBackStack = {
                        navController.navigate(Graph.MyGraph) {
                            popUpTo(0) { inclusive = true } // 모든 백스택 제거
                            launchSingleTop = true          // 중복 방지
                        }
                    },
                )
            }
            composable<Screen.LocationNav> {
                val viewModel: LoginViewModel = hiltViewModel()
                val uiState by viewModel.uiState.collectAsStateWithLifecycle()
                val address by viewModel.address.collectAsStateWithLifecycle()
                val which by viewModel.which.collectAsStateWithLifecycle()
                LaunchedEffect(true) {
                    viewModel.event.collect {
                        when(it){
                            is UiEvent.ShowSnackbar -> {
                                snackbarHostState.showSnackbar(it.message)
                            }
                        }
                    }
                }
                SetLocationScreen(
                    uiState = uiState,
                    address = address,
                    which = which,
                    popBackStack = {navController.popBackStack()},
                    navigateToHome = {
                        navController.navigate(Graph.HomeGraph)
                    },
                    getAddressByCoord = viewModel::getAddressByCoord,
                    setLocation = viewModel::setLocation,
                    from = true,
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
                            is UiEvent.ShowSnackbar -> {
                                snackbarHostState.showSnackbar(event.message)
                            }
                        }
                    }
                }
                MyLikedPostScreen(
                    navigate = {
                        navController.navigate(it)
                    },
                    postItems = postItems,
                    popBackStack = {
                        navController.popBackStack()
                    },
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
                            is UiEvent.ShowSnackbar -> {
                                snackbarHostState.showSnackbar(event.message)
                            }
                        }
                    }
                }
                MyPostScreen(
                    navigate = {
                        navController.navigate(it)
                    },
                    postItems = postItems,
                    popBackStack = {
                        navController.popBackStack()
                    },
                    toggleLike = viewModel::toggleLike,
                )
            }
            composable<Screen.ProfileModifyNav> {
                val parentEntry = navController.getBackStackEntry(Graph.MyGraph)
                val viewModel: ProfileViewModel = hiltViewModel(parentEntry)
                val uiState by viewModel.uiState.collectAsStateWithLifecycle()
                val args = it.toRoute<Screen.ProfileModifyNav>()
                ProfileModifyScreen(
                    uiState = uiState,
                    myName = args.name,
                    imageUrl = args.imageUrl,
                    uploadProfileImage = viewModel::uploadProfileImage,
                    popBackStack = {navController.popBackStack()},
                    deleteProfileImage = viewModel::deleteProfileImage,
                    modifyNickname = viewModel::modifyNickname
                )
            }
        }
        composable<Screen.NotificationNav> {
            val viewModel: NotificationViewModel = hiltViewModel()
            val uiState by viewModel.uiState.collectAsStateWithLifecycle()
            val notificationList by viewModel.notificationList.collectAsStateWithLifecycle()
            NotificationScreen(
                uiState = uiState,
                notificationList = notificationList,
                popBackStack = {navController.popBackStack()}
            )
        }
        navigation<Graph.LoginGraph>(startDestination = Screen.LoginNav){
            composable<Screen.LoginNav> {
                val viewModel: LoginViewModel = hiltViewModel()
                val uiState by viewModel.uiState.collectAsStateWithLifecycle()
                val accountInfo by viewModel.accountInfo.collectAsStateWithLifecycle()
                LaunchedEffect(true) {
                    viewModel.event.collect { event ->
                        when(event){
                            is UiEvent.ShowSnackbar -> {
                                snackbarHostState.showSnackbar(event.message)
                            }
                        }
                    }
                }
                LoginScreen(
                    uiState = uiState,
                    login = viewModel::login,
                    accountInfo = accountInfo,
                    navigateToHome = {
                        navController.navigate(Graph.HomeGraph) {
                            popUpTo(0) { inclusive = true }
                            launchSingleTop = true
                        }
                    },
                    navigateToNext = {
                        navController.navigate(Screen.NicknameNav)
                    },
                )
            }
            composable<Screen.NicknameNav>{
                val viewModel: LoginViewModel = hiltViewModel()
                val uiState by viewModel.uiState.collectAsStateWithLifecycle()
                LaunchedEffect(true) {
                    viewModel.event.collect {
                        when(it){
                            is UiEvent.ShowSnackbar -> {
                                snackbarHostState.showSnackbar(it.message)
                            }
                        }
                    }
                }
                SetNickNameScreen(
                    uiState = uiState,
                    navigateToLoc = {
                        navController.navigate(Screen.LocationNav)
                    },
                    popBackStack = {
                        navController.popBackStack()
                    },
                    setNickname = viewModel::setNickname,
                )
            }
            composable<Screen.LocationNav> {
                val viewModel: LoginViewModel = hiltViewModel()
                val uiState by viewModel.uiState.collectAsStateWithLifecycle()
                val address by viewModel.address.collectAsStateWithLifecycle()
                val which by viewModel.which.collectAsStateWithLifecycle()
                LaunchedEffect(true) {
                    viewModel.event.collect {
                        when(it){
                            is UiEvent.ShowSnackbar -> {
                                snackbarHostState.showSnackbar(it.message)
                            }
                        }
                    }
                }
                SetLocationScreen(
                    uiState = uiState,
                    address = address,
                    which = which,
                    popBackStack = {navController.popBackStack()},
                    navigateToHome = {
                        navController.navigate(Graph.HomeGraph)
                    },
                    getAddressByCoord = viewModel::getAddressByCoord,
                    setLocation = viewModel::setLocation,
                    from = false,
                )
            }
        }
        navigation<Graph.OnBoardingGraph>(startDestination = Screen.OnBoarding) {
            composable<Screen.OnBoarding>{
                LaunchedEffect(true) {
                    mainViewModel.authEvent.collect { event ->
                        when (event) {
                            is AuthEvent.Login -> {
                                navController.navigate(route = Graph.HomeGraph){
                                    popUpTo(0) { inclusive = true } // 모든 백스택 제거
                                    launchSingleTop = true          // 중복 방지
                                }
                            }
                            is AuthEvent.Logout -> {
                                navController.navigate(route = Graph.LoginGraph)
                            }
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

