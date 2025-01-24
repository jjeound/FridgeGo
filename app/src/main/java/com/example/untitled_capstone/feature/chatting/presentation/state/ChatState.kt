package com.example.untitled_capstone.feature.chatting.presentation.state

import com.example.untitled_capstone.feature.chatting.domain.model.ChattingRoom

data class ChatState(
    val chats: List<ChattingRoom> = emptyList(),
    val isLoading: Boolean = false,
)
