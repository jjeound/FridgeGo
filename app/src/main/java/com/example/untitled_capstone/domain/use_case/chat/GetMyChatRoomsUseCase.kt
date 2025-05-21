package com.example.untitled_capstone.domain.use_case.chat

import com.example.untitled_capstone.core.util.Resource
import com.example.untitled_capstone.domain.model.ChattingRoomRaw
import com.example.untitled_capstone.domain.repository.ChatRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.collectLatest
import javax.inject.Inject

class GetMyChatRoomsUseCase @Inject constructor(
    private val repository: ChatRepository
) {
    operator fun invoke(): Flow<Resource<List<ChattingRoomRaw>>> = channelFlow {
        send(Resource.Loading())
        repository.getMyRooms().collectLatest {
            when(it){
                is Resource.Success -> {
                    val sorted = it.data?.sortedWith(
                        compareByDescending<ChattingRoomRaw> { it.active }
                            .thenByDescending { it.lastMessageTime ?: it.createdAt }
                    ) ?: emptyList()
                    send(Resource.Success(sorted))
                }

                is Resource.Error -> {
                    send(Resource.Error(it.message ?: "Unknown error"))
                }

                is Resource.Loading -> {
                    send(Resource.Loading())
                }
            }
        }
    }
}