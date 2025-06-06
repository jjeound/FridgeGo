package com.stone.fridge.domain.use_case.chat

import com.stone.fridge.core.util.Resource
import com.stone.fridge.domain.model.Message
import com.stone.fridge.domain.repository.ChatRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetMessagesUseCase @Inject constructor(
    private val repository: ChatRepository
) {
    operator fun invoke(id: Long, lastMessageId: Long?): Flow<Resource<List<Message>>> {
        return repository.getMessages(id, lastMessageId)
    }
}