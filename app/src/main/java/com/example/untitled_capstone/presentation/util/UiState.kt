package com.example.untitled_capstone.presentation.util

interface UiState {
    data object Success : UiState
    data object Loading : UiState
    data class Error(val message: String?) : UiState
}