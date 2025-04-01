package com.example.untitled_capstone.presentation.feature.login.state

data class ValidateState(
    val validate: Boolean = false,
    val loading: Boolean = false,
    val error: String? = null
)