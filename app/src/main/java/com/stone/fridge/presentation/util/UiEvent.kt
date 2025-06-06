package com.stone.fridge.presentation.util

sealed class UiEvent{
    data class ShowSnackbar(val message: String) : UiEvent()
}