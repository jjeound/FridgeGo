package com.stone.fridge.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.exclude
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.NavigationRailItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.WindowAdaptiveInfo
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteDefaults
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffold
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffoldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.stone.fridge.core.designsystem.theme.CustomTheme
import com.stone.fridge.core.designsystem.R
import com.stone.fridge.core.navigation.AppComposeNavigator
import com.stone.fridge.core.navigation.GoBaseRoute
import com.stone.fridge.core.navigation.GoScreen
import com.stone.fridge.core.ui.CustomSnackbar
import com.stone.fridge.feature.fridge.navigation.navigateToFridgeCRUD
import com.stone.fridge.feature.post.navigation.navigateToPostCRUD
import com.stone.fridge.navigation.GoNavHost
import com.stone.fridge.navigation.TopLevelDestination

@Composable
fun GoMain(
    composeNavigator: AppComposeNavigator<GoScreen>,
    uiState: MainUiState,
    isUnread: Boolean,
    location: String?,
    shouldShowBottomSheet: Boolean,
    showBottomSheet: () -> Unit,
    hideBottomSheet: () -> Unit,
    updateUnreadNotification: () -> Unit,
    startDestination: GoBaseRoute,
    windowAdaptiveInfo: WindowAdaptiveInfo = currentWindowAdaptiveInfo(),
) {
    val navHostController = rememberNavController()
    val snackbarHostState = remember { SnackbarHostState() }
    val topLevelDestinations: List<TopLevelDestination> = TopLevelDestination.entries
    val navBackStackEntry by navHostController.currentBackStackEntryAsState()
    val route = navBackStackEntry?.destination?.route
    val bottomRoute = route?.split(".")?.lastOrNull()
    val bottomBarDestination = topLevelDestinations.any { bottomRoute.equals(it.route.toString()) }
    val myNavigationSuiteItemColors = NavigationSuiteDefaults.itemColors(
        navigationBarItemColors = NavigationBarItemDefaults.colors(
            selectedIconColor = CustomTheme.colors.iconPrimary,
            unselectedIconColor = CustomTheme.colors.iconDefault,
            selectedTextColor = CustomTheme.colors.iconPrimary,
            unselectedTextColor = CustomTheme.colors.textSecondary,
            indicatorColor = Color.Transparent
        ),
        navigationRailItemColors = NavigationRailItemDefaults.colors(
            selectedIconColor = CustomTheme.colors.iconPrimary,
            unselectedIconColor = CustomTheme.colors.iconDefault,
            selectedTextColor = CustomTheme.colors.iconPrimary,
            unselectedTextColor = CustomTheme.colors.textSecondary,
            indicatorColor = Color.Transparent
        )
    )
    LaunchedEffect(Unit) {
        composeNavigator.handleNavigationCommands(navHostController)
    }
    LaunchedEffect(uiState){
        if(uiState is MainUiState.Error){
            snackbarHostState.showSnackbar(
                message = uiState.message,
                actionLabel = null,
                duration = SnackbarDuration.Short,
            )
        }
    }
    if(bottomBarDestination){
        NavigationSuiteScaffold(
            navigationSuiteItems = {
                topLevelDestinations.forEach { destination ->
                    item(
                        selected = navBackStackEntry?.destination?.hierarchy?.any {
                            it.hasRoute(destination.route::class)
                        } == true,
                        onClick = {
                            navHostController.navigate(destination.route) {
                                launchSingleTop = true
                                restoreState = true
                                popUpTo(navHostController.graph.startDestinationId) {
                                    saveState = true
                                }
                            }
                        },
                        icon = {
                            Icon(
                                imageVector = ImageVector.vectorResource(destination.icon),
                                contentDescription = stringResource(destination.contentDescription),
                            )
                        },
                        label = { Text(stringResource(destination.label)) },
                        alwaysShowLabel = true,
                        colors = myNavigationSuiteItemColors
                    )
                }
            },
            navigationSuiteColors = NavigationSuiteDefaults.colors(
                navigationBarContainerColor = CustomTheme.colors.onSurface,
                navigationRailContainerColor = CustomTheme.colors.onSurface,
            ),
            layoutType = NavigationSuiteScaffoldDefaults
                .calculateFromAdaptiveInfo(windowAdaptiveInfo),
            containerColor = CustomTheme.colors.surface,
        ){
            Scaffold(
                containerColor = CustomTheme.colors.surface,
                contentWindowInsets = WindowInsets(0, 0, 0, 0),
                snackbarHost = {
                    SnackbarHost(
                        snackbarHostState,
                        modifier = Modifier.windowInsetsPadding(
                            WindowInsets.safeDrawing.exclude(
                                WindowInsets.ime,
                            ),
                        ),
                        snackbar = { data ->
                            CustomSnackbar(data)
                        }
                    )
                },
                floatingActionButton = {
                    AnimatedVisibility(visible = bottomBarDestination, enter = fadeIn(), exit = fadeOut()) {
                        when (bottomRoute) {
                            TopLevelDestination.HOME.route.toString() -> {
                                FloatingActionButton(
                                    onClick = {
                                        showBottomSheet()
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
                            TopLevelDestination.POST.route.toString() -> {
                                FloatingActionButton(
                                    onClick = {
                                        if(location != null){
                                            navHostController.navigateToPostCRUD()
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
                            TopLevelDestination.FRIDGE.route.toString()  -> {
                                FloatingActionButton(
                                    onClick = {
                                        navHostController.navigateToFridgeCRUD()
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
            ){ innerPadding ->
                Box(
                    modifier = Modifier.padding(innerPadding)
                ){
                    GoNavHost(
                        navController = navHostController,
                        shouldShowBottomSheet = shouldShowBottomSheet,
                        hideBottomSheet = hideBottomSheet,
                        startDestination = startDestination,
                        isUnread = isUnread,
                        updateUnreadNotification = updateUnreadNotification,
                        onShowSnackbar = { message, action ->
                            snackbarHostState.showSnackbar(
                                message = message,
                                actionLabel = action,
                                duration = SnackbarDuration.Short,
                            )
                        },
                    )
                }
            }
        }
    } else {
        GoNavHost(
            navController = navHostController,
            shouldShowBottomSheet = shouldShowBottomSheet,
            hideBottomSheet = hideBottomSheet,
            startDestination = startDestination,
            isUnread = isUnread,
            updateUnreadNotification = updateUnreadNotification,
            onShowSnackbar = { message, action ->
                snackbarHostState.showSnackbar(
                    message = message,
                    actionLabel = action,
                    duration = SnackbarDuration.Short,
                )
            },
        )
    }
}