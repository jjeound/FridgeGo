package com.example.untitled_capstone.domain.use_case.login

import com.example.untitled_capstone.core.util.Resource
import com.example.untitled_capstone.data.remote.dto.ApiResponse
import com.example.untitled_capstone.domain.repository.LoginRepository
import com.example.untitled_capstone.domain.repository.MyRepository
import javax.inject.Inject

class ModifyNickname @Inject constructor(
    private val repository: LoginRepository
) {
    suspend operator fun invoke(nickname: String): Resource<ApiResponse> {
        return repository.modifyNickname(nickname)
    }
}