package com.example.untitled_capstone.domain.use_case.login

import com.example.untitled_capstone.core.util.Resource
import com.example.untitled_capstone.domain.repository.LoginRepository
import javax.inject.Inject

class ModifyNickname @Inject constructor(
    private val repository: LoginRepository
) {
    suspend operator fun invoke(nickname: String): Resource<String> {
        return repository.modifyNickname(nickname)
    }
}