package com.example.untitled_capstone.domain.use_case.chat

import com.example.untitled_capstone.domain.model.Message
import com.example.untitled_capstone.domain.model.UnreadBroadcast
import com.example.untitled_capstone.domain.repository.WebSocketRepository
import javax.inject.Inject

class SubscribeRoom @Inject constructor(
    private val repository: WebSocketRepository
) {
    suspend operator fun invoke(roomId: Long,
                                onMessage: (Message) -> Unit,
                                onUnreadUpdate: (UnreadBroadcast) -> Unit) {
        repository.subscribeRoom(roomId, onMessage = {
            onMessage(it.toMessage())
        } , onUnreadUpdate)
    }
}