package com.example.untitled_capstone.domain.use_case.login

import com.example.untitled_capstone.core.util.Resource
import com.example.untitled_capstone.domain.repository.LoginRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SetLocationUseCase @Inject constructor(
    private val loginRepository: LoginRepository
) {
    suspend operator fun invoke(district: String, neighborhood: String): Flow<Resource<String>> {
        return loginRepository.setLocation(district, neighborhood)
    }
}