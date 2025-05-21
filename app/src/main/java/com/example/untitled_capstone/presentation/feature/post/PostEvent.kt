package com.example.untitled_capstone.presentation.feature.post

import com.example.untitled_capstone.navigation.Screen

interface PostEvent{
    data class ShowSnackbar(val message: String) : PostEvent
    data class Navigate(val route: Screen) : PostEvent
    object PopBackStack : PostEvent
    object ClearBackStack : PostEvent
}