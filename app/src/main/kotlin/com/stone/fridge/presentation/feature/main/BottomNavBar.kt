package com.stone.fridge.presentation.feature.main

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavHostController
import com.stone.fridge.ui.theme.CustomTheme
import androidx.navigation.NavDestination.Companion.hasRoute

@Composable
fun BottomNavBar(currentDestination: NavDestination?, navController: NavHostController){
    val items = listOf(
        BottomNavItems.Home,
        BottomNavItems.Post,
        BottomNavItems.Fridge,
        BottomNavItems.Chat,
        BottomNavItems.My
    )
    NavigationBar(
        modifier = Modifier.height(80.dp).consumeWindowInsets(
            WindowInsets.navigationBars.asPaddingValues()
        ),
        containerColor = CustomTheme.colors.onSurface
    ) {
        items.forEach { destination ->
            NavigationBarItem(
                selected = currentDestination?.hierarchy?.any {
                    it.hasRoute(destination.route::class)
                } == true,
                onClick = {
                    navController.navigate(destination.route)
                },
                icon = {
                    Icon(
                        imageVector = ImageVector.vectorResource(destination.icon),
                        contentDescription = destination.title
                    )
                },
                label = {
                    Text(
                        text = destination.title,
                        style = CustomTheme.typography.caption1
                    )
                },
                colors = NavigationBarItemDefaults.colors(
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