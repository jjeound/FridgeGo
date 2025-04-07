package com.example.untitled_capstone.domain.use_case.chat

import com.example.untitled_capstone.domain.repository.WebSocketRepository
import javax.inject.Inject

class ConnectChatSocket @Inject constructor(
    private val repository: WebSocketRepository
) {
    operator fun invoke(token: String, roomId: Long, onConnected: () -> Unit, onError: (Throwable) -> Unit) {
        repository.connect(token, roomId, onConnected, onError)
    }
}