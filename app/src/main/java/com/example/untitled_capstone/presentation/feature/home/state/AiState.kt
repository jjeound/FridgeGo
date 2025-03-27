package com.example.untitled_capstone.presentation.feature.home.state

import androidx.compose.runtime.mutableStateOf

class AiState{
    val response = mutableStateOf(emptyList<String>())
    val isLoading = mutableStateOf(false)
    val error = mutableStateOf("")
}