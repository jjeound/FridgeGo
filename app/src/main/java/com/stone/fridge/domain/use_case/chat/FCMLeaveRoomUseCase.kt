package com.stone.fridge.domain.use_case.chat

import com.stone.fridge.domain.repository.WebSocketRepository
import javax.inject.Inject

class FCMLeaveRoomUseCase @Inject constructor(
    private val repository: WebSocketRepository
) {
    operator fun invoke(roomId: Long) {
        repository.leaveRoom(roomId)
    }
}