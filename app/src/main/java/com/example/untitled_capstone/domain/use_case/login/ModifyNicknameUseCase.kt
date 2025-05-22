package com.example.untitled_capstone.domain.use_case.login

import com.example.untitled_capstone.core.util.Resource
import com.example.untitled_capstone.domain.repository.LoginRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ModifyNicknameUseCase @Inject constructor(
    private val repository: LoginRepository
) {
    suspend operator fun invoke(nickname: String): Flow<Resource<String>> {
        return repository.modifyNickname(nickname)
    }
}