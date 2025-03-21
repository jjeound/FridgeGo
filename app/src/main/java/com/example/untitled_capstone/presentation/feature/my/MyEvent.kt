package com.example.untitled_capstone.presentation.feature.my

import com.example.untitled_capstone.data.remote.dto.KakaoAccessTokenRequest

sealed interface MyEvent{
    data object Logout: MyEvent
    data object GetMyProfile: MyEvent
}