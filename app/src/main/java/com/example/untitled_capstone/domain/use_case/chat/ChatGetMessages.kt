package com.example.untitled_capstone.domain.use_case.chat

import com.example.untitled_capstone.core.util.Resource
import com.example.untitled_capstone.domain.model.Message
import com.example.untitled_capstone.domain.repository.ChatRepository
import javax.inject.Inject

class ChatGetMessages @Inject constructor(
    private val repository: ChatRepository
) {
    suspend operator fun invoke(id: Long, lastMessageId: Long?): Resource<List<Message>> {
        return repository.getMessages(id, lastMessageId)
    }
}