package com.example.untitled_capstone.presentation.feature.login

import com.example.untitled_capstone.domain.model.AccountInfo

data class LoginState(
    val response: AccountInfo? = null,
    val validate: Boolean = false,
    val loading: Boolean = false,
    val error: String? = null
)