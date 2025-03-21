package com.example.untitled_capstone.presentation.feature.my

import com.example.untitled_capstone.domain.model.Profile

data class MyState(
    val isLoggedIn: Boolean = false,
    val profile: Profile? = null,
    val loading: Boolean = false,
    val error: String? = null
)