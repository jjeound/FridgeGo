package com.example.untitled_capstone.presentation.feature.chat.state

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.example.untitled_capstone.domain.model.ChattingRoomRaw

class ChattingRoomRawState {
    var response by mutableStateOf<List<ChattingRoomRaw>>(emptyList())
    var isLoading by mutableStateOf(false)
    var error by mutableStateOf<String?>(null)
}