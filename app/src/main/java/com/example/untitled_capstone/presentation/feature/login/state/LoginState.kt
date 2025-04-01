package com.example.untitled_capstone.presentation.feature.login.state

import com.example.untitled_capstone.domain.model.AccountInfo

data class LoginState(
    val response: AccountInfo? = null,
    val loading: Boolean = false,
    val error: String? = null
)