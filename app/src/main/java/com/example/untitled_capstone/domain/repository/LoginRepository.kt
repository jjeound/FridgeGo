package com.example.untitled_capstone.domain.repository

import com.example.untitled_capstone.core.util.Resource
import com.example.untitled_capstone.data.remote.dto.ApiResponse
import com.example.untitled_capstone.data.remote.dto.KakaoAccessTokenRequest
import com.example.untitled_capstone.domain.model.AccountInfo

interface LoginRepository {

    suspend fun kakaoLogin(accessToken: KakaoAccessTokenRequest): Resource<AccountInfo>

    suspend fun setNickname(nickname: String): Resource<ApiResponse>
}
