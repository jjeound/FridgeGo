package com.example.untitled_capstone.domain.use_case.login

import com.example.untitled_capstone.core.util.Resource
import com.example.untitled_capstone.data.remote.dto.ApiResponse
import com.example.untitled_capstone.data.remote.dto.LocationDto
import com.example.untitled_capstone.domain.model.Address
import com.example.untitled_capstone.domain.repository.LoginRepository
import javax.inject.Inject

class SetLocation @Inject constructor(
    private val loginRepository: LoginRepository
) {
    suspend operator fun invoke(district: String, neighborhood: String): Resource<ApiResponse> {
        return loginRepository.setLocation(LocationDto(district, neighborhood))
    }
}