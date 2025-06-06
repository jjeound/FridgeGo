package com.stone.fridge.domain.use_case.my

import com.stone.fridge.domain.repository.MyRepository
import javax.inject.Inject

class GetMyNicknameUseCase @Inject constructor(
    private val myRepository: MyRepository
) {
    suspend operator fun invoke(): String? {
        return myRepository.getNickname()
    }
}