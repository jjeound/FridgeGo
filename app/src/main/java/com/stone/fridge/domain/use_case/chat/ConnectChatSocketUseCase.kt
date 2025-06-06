package com.stone.fridge.domain.use_case.chat

import com.stone.fridge.domain.repository.WebSocketRepository
import javax.inject.Inject

class ConnectChatSocketUseCase @Inject constructor(
    private val repository: WebSocketRepository
) {
    operator fun invoke(token: String, roomId: Long, onConnected: () -> Unit, onError: (Throwable) -> Unit) {
        repository.connect(token, roomId, onConnected, onError)
    }
}