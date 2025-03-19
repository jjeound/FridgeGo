package com.example.untitled_capstone.presentation.feature.login

import com.example.untitled_capstone.data.remote.dto.LoginCallbackResponse

data class KakakLoginState(
    val response: LoginCallbackResponse? = null,
    val loading: Boolean = false,
    val error: String = ""
)