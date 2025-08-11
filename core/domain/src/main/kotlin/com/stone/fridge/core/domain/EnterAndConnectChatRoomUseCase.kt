package com.stone.fridge.core.domain

import com.stone.fridge.core.data.chat.ChatRepository
import com.stone.fridge.core.model.ChatRoom
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class EnterAndConnectChatRoomUseCase @Inject constructor(
    private val chatRepository: ChatRepository
) {
    operator fun invoke(roomId: Long, onConnected: () -> Unit, onError: (Throwable) -> Unit): Flow<ChatRoom> = flow {
        // 1. 내 채팅방 목록 확인
        val rooms = chatRepository.getMyRooms().first()
        val isJoined = rooms.any { it.roomId == roomId }

        // 2. 채팅방 정보 받아오기
        val chatRoom = chatRepository.enterChatRoom(roomId).first()
        emit(chatRoom)

        // 3. 참가 안했으면 join
        if (!isJoined) {
            chatRepository.joinChatRoom(roomId).first()
        }

        // 4. WebSocket 연결
        chatRepository.connect(
            roomId,
            onConnected = onConnected,
            onError = onError
        )
    }
}