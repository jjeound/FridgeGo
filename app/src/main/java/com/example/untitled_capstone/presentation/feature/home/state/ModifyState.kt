package com.example.untitled_capstone.presentation.feature.home.state

import androidx.compose.runtime.mutableStateOf

class ModifyState{
    val isSuccess = mutableStateOf(false)
    val isLoading = mutableStateOf(false)
    val error = mutableStateOf("")
}