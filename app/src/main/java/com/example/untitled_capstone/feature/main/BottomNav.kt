package com.example.untitled_capstone.feature.main

import androidx.compose.ui.graphics.vector.ImageVector

data class BottomNavItem(
    val title: String,
    val route: String,
    val icon: ImageVector,
)

sealed class BottomScreen(val route: String){
    data object Home: BottomScreen("home")
    data object Shopping: BottomScreen("shopping")
    data object Refrigerator: BottomScreen("refrigerator")
    data object Chat: BottomScreen("chatting")
    data object My: BottomScreen("my")
}