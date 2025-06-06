package com.stone.fridge.domain.use_case.login

import com.stone.fridge.core.util.Resource
import com.stone.fridge.domain.repository.LoginRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SetNicknameUseCase @Inject constructor(
    private val loginRepository: LoginRepository
) {
    operator fun invoke(nickname: String): Flow<Resource<String>> {
        return loginRepository.setNickname(nickname)
    }
}