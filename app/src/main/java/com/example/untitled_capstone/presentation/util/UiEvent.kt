package com.example.untitled_capstone.presentation.util

sealed class UiEvent{
    data class ShowSnackbar(val message: String) : UiEvent()
}