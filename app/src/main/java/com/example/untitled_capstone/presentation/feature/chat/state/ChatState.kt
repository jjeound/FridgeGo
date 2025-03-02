package com.example.untitled_capstone.presentation.feature.chat.state

import com.example.untitled_capstone.domain.model.ChattingRoom

data class ChatState(
    val chats: List<ChattingRoom> = emptyList(),
    val isLoading: Boolean = false,
)
