package com.stone.fridge.domain.use_case.login

import com.stone.fridge.core.util.Resource
import com.stone.fridge.domain.model.AccountInfo
import com.stone.fridge.domain.repository.LoginRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class KakaoLoginUseCase @Inject constructor(
    private val loginRepository: LoginRepository
) {
    operator fun invoke(accessToken: String): Flow<Resource<AccountInfo>> {
        return loginRepository.kakaoLogin(accessToken)
    }
}