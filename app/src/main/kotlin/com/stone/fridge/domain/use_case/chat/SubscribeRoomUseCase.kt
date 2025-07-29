package com.stone.fridge.domain.use_case.chat

import com.stone.fridge.domain.model.Message
import com.stone.fridge.domain.model.UnreadBroadcast
import com.stone.fridge.domain.repository.WebSocketRepository
import javax.inject.Inject

class SubscribeRoomUseCase @Inject constructor(
    private val repository: WebSocketRepository
) {
    suspend operator fun invoke(
        roomId: Long,
        onUnreadUpdate: (UnreadBroadcast) -> Unit
    ) {
        repository.subscribeRoom(roomId, onUnreadUpdate = { onUnreadUpdate(it.toUnreadBroadcast()) })
    }
}