package com.example.untitled_capstone.presentation.feature.main

import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
import com.example.untitled_capstone.R
import com.example.untitled_capstone.navigation.Screen
import com.example.untitled_capstone.ui.theme.CustomTheme
import androidx.navigation.NavDestination.Companion.hasRoute

@Composable
fun BottomNavBar(currentDestination: NavDestination?, navController: NavHostController){
    val items = listOf(
        BottomNavItem(
            title = "홈",
            route = Screen.Home,
            icon = ImageVector.vectorResource(id = R.drawable.home)
        ),
        BottomNavItem(
            title = "공동구매",
            route = Screen.Shopping,
            icon = ImageVector.vectorResource(id = R.drawable.shopping)
        ),
        BottomNavItem(
            title = "냉장고",
            route = Screen.Fridge,
            icon = ImageVector.vectorResource(id = R.drawable.refrigerator)
        ),
        BottomNavItem(
            title = "채팅",
            route = Screen.Chat,
            icon = ImageVector.vectorResource(id = R.drawable.chat)
        ),
        BottomNavItem(
            title = "My",
            route = Screen.My,
            icon = ImageVector.vectorResource(id = R.drawable.my)
        )
    )

    NavigationBar(
        modifier = Modifier.height(80.dp),
        containerColor = CustomTheme.colors.onSurface
    ) {
        items.forEach { destination ->
            NavigationBarItem(
                modifier = Modifier.padding(3.dp).padding(top = 10.dp),
                selected = currentDestination?.hierarchy?.any {
                    it.hasRoute(destination.route::class)
                } ?: false,
                onClick = {
                    navController.navigate(destination.route)
                },
                icon = {
                    Icon(
                        imageVector = destination.icon,
                        contentDescription = destination.title
                    )
                },
                label = {
                    Text(
                        text = destination.title,
                        fontWeight = CustomTheme.typography.button2.fontWeight,
                        fontSize = CustomTheme.typography.button2.fontSize,
                        fontFamily = CustomTheme.typography.button2.fontFamily,
                    )
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = CustomTheme.colors.iconSelected,
                    unselectedIconColor = CustomTheme.colors.iconDefault,
                    selectedTextColor = CustomTheme.colors.textPrimary,
                    unselectedTextColor = CustomTheme.colors.textSecondary,
                    indicatorColor = Color.Transparent
                )
            )
        }
    }
}