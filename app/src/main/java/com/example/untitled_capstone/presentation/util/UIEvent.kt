package com.example.untitled_capstone.presentation.util

import com.example.untitled_capstone.navigation.Screen


sealed class UIEvent{
    data class ShowSnackbar(val message: String) : UIEvent()
    data class Navigate(val route: Screen) : UIEvent()
}