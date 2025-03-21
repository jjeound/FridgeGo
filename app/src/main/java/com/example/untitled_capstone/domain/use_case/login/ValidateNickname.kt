package com.example.untitled_capstone.domain.use_case.login

import com.example.untitled_capstone.core.util.Resource
import com.example.untitled_capstone.data.remote.dto.ApiResponse
import com.example.untitled_capstone.data.remote.dto.CallbackDto
import com.example.untitled_capstone.data.remote.dto.KakaoAccessTokenRequest
import com.example.untitled_capstone.data.remote.dto.ProfileDto
import com.example.untitled_capstone.domain.repository.LoginRepository
import javax.inject.Inject

class ValidateNickname @Inject constructor(
    private val loginRepository: LoginRepository
) {
    suspend operator fun invoke(nickname: String): Resource<ApiResponse> {
        return loginRepository.validateNickname(nickname)
    }
}