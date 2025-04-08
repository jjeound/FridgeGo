package com.example.untitled_capstone.presentation.feature.home.state


data class ModifyState(
    val isSuccess : Boolean = false,
    val loading: Boolean = false,
    val error: String? = null
)