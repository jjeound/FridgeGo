package com.example.untitled_capstone.feature.chatting.presentation.state

import com.example.untitled_capstone.feature.chatting.domain.model.Message

data class MessageState(
    val messages: List<Message> = emptyList(),
    val isLoading: Boolean = false,
)
