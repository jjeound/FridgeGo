package com.example.untitled_capstone.domain.repository

import com.example.untitled_capstone.core.util.Resource
import com.example.untitled_capstone.data.remote.dto.KakaoAccessTokenRequest
import com.example.untitled_capstone.data.remote.dto.LoginCallbackResponse

interface LoginRepository{
    suspend fun loginCallback(accessToken: KakaoAccessTokenRequest): Resource<LoginCallbackResponse>
}