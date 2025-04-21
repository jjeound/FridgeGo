package com.example.untitled_capstone.presentation.feature.chat.state

sealed interface ChatUiState {
    data object Success : ChatUiState
    data object Loading : ChatUiState
    data class Error(val message: String?) : ChatUiState
}