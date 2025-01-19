package com.example.untitled_capstone.feature.main

import androidx.compose.ui.graphics.vector.ImageVector

data class BottomNavItem<T: Any>(
    val title: String,
    val route: T,
    val icon: ImageVector,
)