package com.example.untitled_capstone.feature.main

import androidx.compose.foundation.layout.height
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import com.example.untitled_capstone.R
import com.example.untitled_capstone.feature.chatting.Chatting
import com.example.untitled_capstone.feature.home.presentation.screen.Home
import com.example.untitled_capstone.feature.my.My
import com.example.untitled_capstone.feature.refrigerator.Refrigerator
import com.example.untitled_capstone.feature.shopping.Shopping
import com.example.untitled_capstone.ui.theme.CustomTheme

@Composable
fun BottomNavBar(navController: NavHostController, viewModel: MainViewModel){
    val items = listOf(
        BottomNavItem(
            title = "홈",
            route = Home,
            icon = ImageVector.vectorResource(id = R.drawable.home)
        ),
        BottomNavItem(
            title = "공동구매",
            route = Shopping,
            icon = ImageVector.vectorResource(id = R.drawable.shopping)
        ),
        BottomNavItem(
            title = "냉장고",
            route = Refrigerator,
            icon = ImageVector.vectorResource(id = R.drawable.refrigerator)
        ),
        BottomNavItem(
            title = "채팅",
            route = Chatting,
            icon = ImageVector.vectorResource(id = R.drawable.chat)
        ),
        BottomNavItem(
            title = "My",
            route = My,
            icon = ImageVector.vectorResource(id = R.drawable.my)
        )
    )

    NavigationBar(
        modifier = Modifier.height(94.dp),
        containerColor = CustomTheme.colors.onSurface
    ) {
        items.forEachIndexed() { index, item ->
            NavigationBarItem(
                selected = viewModel.selectedIndex == index,
                onClick = {
                    viewModel.updateSelectedIndex(index)
                    navController.navigate(item.route){
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }

                },
                icon = {
                    Icon(
                        imageVector = item.icon,
                        contentDescription = item.title
                    )
                },
                label = {
                    Text(
                        text = item.title,
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
                )
            )
        }
    }
}