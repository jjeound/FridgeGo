package com.example.untitled_capstone.presentation.feature.main

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
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.untitled_capstone.R
import com.example.untitled_capstone.navigation.Navigation
import com.example.untitled_capstone.navigation.Screen
import com.example.untitled_capstone.presentation.feature.my.MyTopBar
import com.example.untitled_capstone.presentation.feature.fridge.FridgeTopBar
import com.example.untitled_capstone.presentation.feature.post.PostTopBar
import com.example.untitled_capstone.ui.theme.CustomTheme

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

    Scaffold(
        containerColor = CustomTheme.colors.surface,
        snackbarHost = {SnackbarHost(hostState = snackbarHostState)},
        topBar = {
            when{
                bottomRoute.equals(screens[0]) -> TopBar(1, navController)
                bottomRoute.equals(screens[1]) -> PostTopBar(navController)
                bottomRoute.equals(screens[2]) -> FridgeTopBar(navController, viewModel)
                bottomRoute.equals(screens[3]) -> TopBar(4, navController)
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
                                tint = CustomTheme.colors.iconPrimary,
                                contentDescription = "ai"
                            )
                        }
                    }
                    screens[1], screens[2] -> {
                        FloatingActionButton(
                            onClick = {
                                if (bottomRoute == screens[1]) navController.navigate(Screen.WritingNav)
                                else navController.navigate(Screen.AddFridgeItemNav(
                                    id = null
                                ))
                            },
                            elevation = FloatingActionButtonDefaults.elevation(0.dp),
                            containerColor = Color.Unspecified,
                            contentColor = Color.Unspecified
                        ) {
                            Icon(
                                imageVector = ImageVector.vectorResource(id = R.drawable.writing),
                                tint = Color.Unspecified,
                                contentDescription = "write new post"
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