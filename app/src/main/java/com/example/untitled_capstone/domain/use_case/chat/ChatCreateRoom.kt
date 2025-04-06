package com.example.untitled_capstone.domain.use_case.chat

import com.example.untitled_capstone.core.util.Resource
import com.example.untitled_capstone.data.remote.dto.NewChatRoomBody
import com.example.untitled_capstone.domain.model.ChattingRoom
import com.example.untitled_capstone.domain.repository.ChatRepository
import javax.inject.Inject

class ChatCreateRoom @Inject constructor(
    private val repository: ChatRepository
) {
    suspend operator fun invoke(name: String, maxParticipants: Int): Resource<ChattingRoom> {
        return repository.createChatRoom(NewChatRoomBody(name, maxParticipants))
    }
}