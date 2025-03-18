package com.example.untitled_capstone.domain.repository

import com.example.untitled_capstone.core.util.Resource
import com.example.untitled_capstone.data.remote.dto.KakaoLoginResponse

interface KakaoLoginRepository{
    suspend fun kakaoLoginCallback(code: String): Resource<KakaoLoginResponse>
    suspend fun whoami(): Resource<Unit>
}