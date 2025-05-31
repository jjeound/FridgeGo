package com.example.untitled_capstone.domain.use_case.chat

import com.example.untitled_capstone.core.util.Resource
import com.example.untitled_capstone.domain.repository.ChatRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class CloseChatRoomUseCase @Inject constructor(
    private val repository: ChatRepository
) {
    operator fun invoke(id: Long): Flow<Resource<String>> {
        return repository.closeChatRoom(id)
    }
}