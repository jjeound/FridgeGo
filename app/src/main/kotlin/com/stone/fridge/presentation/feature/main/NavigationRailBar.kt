package com.stone.fridge.presentation.feature.main

import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.NavigationRailItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavHostController
import com.stone.fridge.ui.theme.CustomTheme

@Composable
fun NavigationRailBar(currentDestination: NavDestination?, navController: NavHostController, modifier: Modifier) {
    val items = listOf(
        BottomNavItems.Home,
        BottomNavItems.Post,
        BottomNavItems.Fridge,
        BottomNavItems.Chat,
        BottomNavItems.My
    )
    NavigationRail(
        modifier = modifier
            .fillMaxHeight(),
        containerColor = CustomTheme.colors.onSurface
    ) {
        items.forEach { destination ->
            NavigationRailItem(
                selected = currentDestination?.hierarchy?.any {
                    it.hasRoute(destination.route::class)
                } == true,
                onClick = {
                    navController.navigate(destination.route) {
                        launchSingleTop = true
                        restoreState = true
                        popUpTo(navController.graph.startDestinationId) {
                            saveState = true
                        }
                    }
                },
                icon = {
                    Icon(
                        imageVector = ImageVector.vectorResource(id = destination.icon),
                        contentDescription = destination.title
                    )
                },
                label = {
                    Text(
                        text = destination.title,
                        style = CustomTheme.typography.caption1
                    )
                },
                alwaysShowLabel = true,
                colors = NavigationRailItemDefaults.colors(
                    selectedIconColor = CustomTheme.colors.iconPrimary,
                    unselectedIconColor = CustomTheme.colors.iconDefault,
                    selectedTextColor = CustomTheme.colors.iconPrimary,
                    unselectedTextColor = CustomTheme.colors.textSecondary,
                    indicatorColor = Color.Transparent
                )
            )
        }
    }
}