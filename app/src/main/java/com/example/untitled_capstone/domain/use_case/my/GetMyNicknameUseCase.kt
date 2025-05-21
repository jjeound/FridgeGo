package com.example.untitled_capstone.domain.use_case.my

import com.example.untitled_capstone.domain.repository.MyRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetMyNicknameUseCase @Inject constructor(
    private val myRepository: MyRepository
) {
    suspend operator fun invoke(): String? {
        return myRepository.getNickname()
    }
}