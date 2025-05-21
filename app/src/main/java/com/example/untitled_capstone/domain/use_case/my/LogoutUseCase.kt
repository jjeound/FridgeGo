package com.example.untitled_capstone.domain.use_case.my

import com.example.untitled_capstone.core.util.Resource
import com.example.untitled_capstone.domain.repository.MyRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class LogoutUseCase @Inject constructor(
    private val myRepository: MyRepository
) {
    suspend operator fun invoke(): Flow<Resource<String>> {
        return myRepository.logout()
    }
}