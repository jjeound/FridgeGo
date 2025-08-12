package com.stone.fridge.core.domain

import com.stone.fridge.core.data.chat.ChatRepository
import com.stone.fridge.core.model.ChatRoomRaw
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import kotlin.collections.sortedWith

class GetSortedMyChatRoomsUseCase @Inject constructor(
    private val chatRepository: ChatRepository
) {
    operator fun invoke(): Flow<List<ChatRoomRaw>> =  chatRepository.getMyRooms().map {
        it.sortedWith(compareByDescending<ChatRoomRaw> { room ->
            room.active
        }.thenByDescending { room ->
            room.lastMessageTime ?: room.createdAt
        })
    }
}