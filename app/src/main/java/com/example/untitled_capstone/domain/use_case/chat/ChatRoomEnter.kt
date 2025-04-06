package com.example.untitled_capstone.domain.use_case.chat

import com.example.untitled_capstone.core.util.Resource
import com.example.untitled_capstone.domain.model.ChattingRoom
import com.example.untitled_capstone.domain.repository.ChatRepository
import javax.inject.Inject

class ChatRoomEnter @Inject constructor(
    private val repository: ChatRepository
) {
    suspend operator fun invoke(id: Long): Resource<ChattingRoom> {
        return repository.enterChatRoom(id)
    }
}