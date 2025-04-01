package com.example.untitled_capstone.presentation.feature.my


sealed interface MyEvent{
    data object Logout: MyEvent
    data object GetMyProfile: MyEvent
}