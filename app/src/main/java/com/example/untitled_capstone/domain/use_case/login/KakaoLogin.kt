package com.example.untitled_capstone.domain.use_case.login

import com.example.untitled_capstone.core.util.Resource
import com.example.untitled_capstone.domain.model.AccountInfo
import com.example.untitled_capstone.domain.repository.LoginRepository
import javax.inject.Inject

class KakaoLogin @Inject constructor(
    private val loginRepository: LoginRepository
) {
    suspend operator fun invoke(accessToken: String): Resource<AccountInfo> {
        return loginRepository.kakaoLogin(accessToken)
    }
}