package com.stone.fridge.domain.use_case.chat

import com.stone.fridge.domain.repository.ChatRepository
import javax.inject.Inject

class GetUserIdUseCase @Inject constructor(
    private val repository: ChatRepository
) {
    suspend operator fun invoke(): Long? {
        return repository.getUserId()
    }
}