package com.example.untitled_capstone.presentation.feature.main

import com.example.untitled_capstone.R
import com.example.untitled_capstone.navigation.Screen

sealed class BottomNavItems(val title: String, val route: Screen, val icon: Int){
    data object Home: BottomNavItems("홈", Screen.Home, R.drawable.home)
    data object Post: BottomNavItems("공동구매", Screen.Post, R.drawable.shopping)
    data object Fridge: BottomNavItems("냉장고", Screen.Fridge, R.drawable.refrigerator)
    data object Chat: BottomNavItems("채팅", Screen.Chat, R.drawable.chat)
    data object My: BottomNavItems("My", Screen.My, R.drawable.my)
}