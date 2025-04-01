package com.example.untitled_capstone.domain.use_case.login

import com.example.untitled_capstone.core.util.Resource
import com.example.untitled_capstone.domain.model.Address
import com.example.untitled_capstone.domain.repository.LoginRepository
import javax.inject.Inject

class GetAddressByCoord @Inject constructor(
    private val loginRepository: LoginRepository
) {
    suspend operator fun invoke(x: String, y: String): Resource<Address> {
        return loginRepository.getAddressByCoord(x, y)
    }
}