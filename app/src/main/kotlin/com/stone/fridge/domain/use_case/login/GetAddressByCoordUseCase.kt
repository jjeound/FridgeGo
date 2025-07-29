package com.stone.fridge.domain.use_case.login

import com.stone.fridge.core.util.Resource
import com.stone.fridge.domain.model.Address
import com.stone.fridge.domain.repository.LoginRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetAddressByCoordUseCase @Inject constructor(
    private val loginRepository: LoginRepository
) {
    operator fun invoke(x: String, y: String): Flow<Resource<Address>> {
        return loginRepository.getAddressByCoord(x, y)
    }
}