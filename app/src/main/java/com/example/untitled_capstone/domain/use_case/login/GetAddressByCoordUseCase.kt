package com.example.untitled_capstone.domain.use_case.login

import com.example.untitled_capstone.core.util.Resource
import com.example.untitled_capstone.domain.model.Address
import com.example.untitled_capstone.domain.repository.LoginRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetAddressByCoordUseCase @Inject constructor(
    private val loginRepository: LoginRepository
) {
    operator fun invoke(x: String, y: String): Flow<Resource<Address>> {
        return loginRepository.getAddressByCoord(x, y)
    }
}