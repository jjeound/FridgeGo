package com.example.untitled_capstone.domain.use_case.login

import com.example.untitled_capstone.core.util.Resource
import com.example.untitled_capstone.domain.repository.LoginRepository
import javax.inject.Inject

class SetNickname @Inject constructor(
    private val loginRepository: LoginRepository
) {
    suspend operator fun invoke(nickname: String): Resource<String> {
        return loginRepository.setNickname(nickname)
    }
}