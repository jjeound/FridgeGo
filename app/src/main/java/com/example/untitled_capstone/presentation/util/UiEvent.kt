package com.example.untitled_capstone.presentation.util

import com.example.untitled_capstone.navigation.Screen


sealed class UiEvent{
    data class ShowSnackbar(val message: String) : UiEvent()
    data class Navigate(val route: Screen) : UiEvent()
}