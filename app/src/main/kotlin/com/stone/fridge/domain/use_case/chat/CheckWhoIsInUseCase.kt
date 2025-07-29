package com.stone.fridge.domain.use_case.chat

import com.stone.fridge.core.util.Resource
import com.stone.fridge.domain.model.ChatMember
import com.stone.fridge.domain.repository.ChatRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class CheckWhoIsInUseCase @Inject constructor(
    private val repository: ChatRepository
) {
    operator fun invoke(id: Long): Flow<Resource<List<ChatMember>>> {
        return repository.checkWhoIsIn(id)
    }
}