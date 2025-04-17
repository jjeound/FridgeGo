package com.example.untitled_capstone.presentation.feature.login

sealed interface LoginEvent{
    data class KakaoLogin(val accessToken: String): LoginEvent
    data class SetNickname(val nickname: String): LoginEvent
    data class GetAddressByCoord(val x: String, val y: String): LoginEvent
    data class ModifyNickname(val nickname: String): LoginEvent
    data class SetAddress(val district: String, val neighborhood: String): LoginEvent
}