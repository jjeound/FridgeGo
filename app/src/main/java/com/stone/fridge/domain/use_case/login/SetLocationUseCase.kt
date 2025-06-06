package com.stone.fridge.domain.use_case.login

import com.stone.fridge.core.util.Resource
import com.stone.fridge.domain.repository.LoginRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SetLocationUseCase @Inject constructor(
    private val loginRepository: LoginRepository
) {
    operator fun invoke(district: String, neighborhood: String): Flow<Resource<String>> {
        return loginRepository.setLocation(district, neighborhood)
    }
}