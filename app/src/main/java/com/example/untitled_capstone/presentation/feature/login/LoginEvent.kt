package com.example.untitled_capstone.presentation.feature.login

import com.example.untitled_capstone.data.remote.dto.KakaoAccessTokenRequest


sealed interface LoginEvent{
    data class KakaoLogin(val accessToken: KakaoAccessTokenRequest): LoginEvent
    data class SetNickname(val nickname: String): LoginEvent
}