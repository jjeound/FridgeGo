package com.example.untitled_capstone.domain.use_case.chat

import com.example.untitled_capstone.domain.repository.WebSocketRepository
import javax.inject.Inject

class SendReadEventUseCase @Inject constructor(
    private val repository: WebSocketRepository
) {
    operator fun invoke(roomId: Long){
        repository.sendReadEvent(roomId)
    }
}