package com.example.untitled_capstone.domain.use_case.kakao

import com.example.untitled_capstone.core.util.Resource
import com.example.untitled_capstone.data.remote.dto.KakaoLoginResponse
import com.example.untitled_capstone.domain.repository.KakaoLoginRepository
import javax.inject.Inject

class KakaoLoginCallback @Inject constructor(
    private val loginRepository: KakaoLoginRepository
) {
    suspend operator fun invoke(code: String): Resource<KakaoLoginResponse> {
        return loginRepository.kakaoLoginCallback(code)
    }
}