package com.stone.fridge.presentation.feature.main

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.stone.fridge.R
import com.stone.fridge.core.util.Dimens
import com.stone.fridge.navigation.Navigation
import com.stone.fridge.navigation.Screen
import com.stone.fridge.presentation.feature.my.MyTopBar
import com.stone.fridge.presentation.feature.fridge.FridgeTopBar
import com.stone.fridge.presentation.feature.post.PostTopBar
import com.stone.fridge.presentation.util.CustomSnackbar
import com.stone.fridge.ui.theme.CustomTheme

@Composable
fun MainScreen(viewModel: MainViewModel){
    val snackbarHostState = remember { SnackbarHostState() }
    val navController = rememberNavController()
    val screens = listOf(
        Screen.Home.toString(),
        Screen.Post.toString(),
        Screen.Fridge.toString(),
        Screen.Chat.toString(),
        Screen.My.toString()
    )
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination
    val route = navBackStackEntry?.destination?.route
    val bottomRoute = route?.split(".")?.lastOrNull()
    val bottomBarDestination = screens.any { bottomRoute.equals(it) }
    val dong by viewModel.dong.collectAsStateWithLifecycle()
    val isUnread by viewModel.isUnread.collectAsStateWithLifecycle()

    Scaffold(
        containerColor = CustomTheme.colors.surface,
        snackbarHost = {
            Box(modifier = Modifier.fillMaxSize()) {
                SnackbarHost(
                    hostState = snackbarHostState,
                    snackbar = { data -> CustomSnackbar(data) },
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(bottom = Dimens.snackbarPadding)
                )
            }
        },
        topBar = {
            when{
                bottomRoute.equals(screens[0]) -> TopBar(1, navController, isUnread, viewModel::updateUnreadNotification)
                bottomRoute.equals(screens[1]) -> PostTopBar(
                    navigate = { screen ->
                        navController.navigate(screen)
                    },
                    getLocation = viewModel::getLocation,
                    dong,
                    isUnread = isUnread,
                    updateUnreadNotification = viewModel::updateUnreadNotification
                )
                bottomRoute.equals(screens[2]) -> FridgeTopBar(navController, viewModel, isUnread, viewModel::updateUnreadNotification)
                bottomRoute.equals(screens[3]) -> TopBar(4, navController, isUnread, viewModel::updateUnreadNotification)
                bottomRoute.equals(screens[4]) -> MyTopBar()
            }
        },
        bottomBar = {
            AnimatedVisibility(
                modifier = Modifier.padding(bottom = WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding() * 0.7f),
                visible = bottomBarDestination, enter = fadeIn(), exit = fadeOut()) {
                BottomNavBar(currentDestination = currentDestination, navController = navController)
            }
        },
        floatingActionButton = {
            AnimatedVisibility(visible = bottomBarDestination, enter = fadeIn(), exit = fadeOut()) {
                when (bottomRoute) {
                    screens[0] -> {
                        FloatingActionButton(
                            onClick = {
                                viewModel.showBottomSheet()
                            },
                            elevation = FloatingActionButtonDefaults.elevation(0.dp),
                            containerColor = Color.Unspecified,
                            contentColor = CustomTheme.colors.iconPrimary,
                        ) {
                            Icon(
                                imageVector = ImageVector.vectorResource(id = R.drawable.ai_big),
                                tint = Color.Unspecified,
                                contentDescription = "ai"
                            )
                        }
                    }
                    screens[1] -> {
                        FloatingActionButton(
                            onClick = {
                                if(dong != null){
                                    navController.navigate(Screen.WritingNav)
                                }
                            },
                            elevation = FloatingActionButtonDefaults.elevation(0.dp),
                            containerColor = Color.Unspecified,
                            contentColor = Color.Unspecified,
                        ) {
                            Icon(
                                imageVector = ImageVector.vectorResource(id = R.drawable.writing),
                                tint = Color.Unspecified,
                                contentDescription = "write new post"
                            )
                        }
                    }
                    screens[2] -> {
                        FloatingActionButton(
                            onClick = {
                                navController.navigate(Screen.AddFridgeItemNav(
                                    id = null, isFridge = viewModel.topSelector
                                ))
                            },
                            elevation = FloatingActionButtonDefaults.elevation(0.dp),
                            containerColor = Color.Unspecified,
                            contentColor = Color.Unspecified
                        ) {
                            Icon(
                                imageVector = ImageVector.vectorResource(id = R.drawable.writing),
                                tint = Color.Unspecified,
                                contentDescription = "write new fridge item"
                            )
                        }
                    }
                }
            }
        },
        floatingActionButtonPosition = FabPosition.End
    ){  innerPadding ->
        if(bottomBarDestination) {
            Box(
                modifier = Modifier.fillMaxSize().padding(innerPadding)
            ){
                Navigation(navController = navController, viewModel, snackbarHostState)
            }
        }else{
            Navigation(navController = navController, viewModel, snackbarHostState)
        }
    }
}