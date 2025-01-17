package com.example.untitled_capstone

import androidx.compose.ui.graphics.vector.ImageVector

data class BottomNavItem<T: Any>(
    val title: String,
    val route: T,
    val icon: ImageVector,
)