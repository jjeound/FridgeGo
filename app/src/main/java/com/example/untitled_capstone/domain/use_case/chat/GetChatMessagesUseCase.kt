package com.example.untitled_capstone.domain.use_case.chat

import androidx.paging.PagingData
import androidx.paging.map
import com.example.untitled_capstone.domain.model.Message
import com.example.untitled_capstone.domain.repository.ChatRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetChatMessagesUseCase @Inject constructor(
    private val repository: ChatRepository
) {
    operator fun invoke(roomId: Long): Flow<PagingData<Message>> {
        return repository.getMessagePaged(roomId).map { pagingData ->
            pagingData.map { it.toMessage() }
        }
    }
}