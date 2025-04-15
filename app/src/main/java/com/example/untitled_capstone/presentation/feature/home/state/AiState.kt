package com.example.untitled_capstone.presentation.feature.home.state

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

class AiState {
    var response by mutableStateOf(emptyList<String>())
    var isLoading by mutableStateOf(false)
    var error by mutableStateOf<String?>(null)
}