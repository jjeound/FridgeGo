package com.example.untitled_capstone.domain.use_case.kakao

import com.example.untitled_capstone.core.util.Resource
import com.example.untitled_capstone.data.remote.dto.KakaoAccessTokenRequest
import com.example.untitled_capstone.data.remote.dto.LoginCallbackResponse
import com.example.untitled_capstone.domain.repository.LoginRepository
import javax.inject.Inject

class LoginCallback @Inject constructor(
    private val loginRepository: LoginRepository
) {
    suspend operator fun invoke(accessToken: KakaoAccessTokenRequest): Resource<LoginCallbackResponse> {
        return loginRepository.loginCallback(accessToken)
    }
}