package com.example.untitled_capstone.presentation.feature.chat.state

sealed interface ChatUiState {
    data class Success<T>(val data: T? = null) : ChatUiState
    data object Loading : ChatUiState
    data class Error(val message: String?) : ChatUiState
}