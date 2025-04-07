package com.example.untitled_capstone.domain.use_case.chat

import com.example.untitled_capstone.data.remote.dto.MessageDto
import com.example.untitled_capstone.data.remote.dto.UnreadBroadcastDto
import com.example.untitled_capstone.domain.repository.WebSocketRepository
import javax.inject.Inject

class Disconnect @Inject constructor(
    private val repository: WebSocketRepository
) {
    operator fun invoke() {
        repository.disconnect()
    }
}