package com.example.untitled_capstone.domain.use_case.chat

import com.example.untitled_capstone.core.util.Resource
import com.example.untitled_capstone.domain.model.ChattingRoomRaw
import com.example.untitled_capstone.domain.repository.ChatRepository
import javax.inject.Inject

class ChatGetMyRooms @Inject constructor(
    private val repository: ChatRepository
) {
    suspend operator fun invoke(): Resource<List<ChattingRoomRaw>> {
        return when (val result = repository.getMyRooms()) {
            is Resource.Success -> {
                result.data?.let { rooms ->
                    val sorted = rooms.sortedWith(
                        compareByDescending<ChattingRoomRaw> { it.active }
                            .thenByDescending {
                                it.lastMessageTime ?: it.createdAt
                            }
                    )
                    Resource.Success(sorted)
                } ?: Resource.Success(emptyList())
            }

            is Resource.Error -> Resource.Error(result.message ?: "Unknown error")
            is Resource.Loading -> Resource.Loading()
        }
    }
}