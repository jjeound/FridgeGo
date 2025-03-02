package com.example.untitled_capstone.presentation.feature.chat.state

import com.example.untitled_capstone.domain.model.Message

data class MessageState(
    val messages: List<Message> = emptyList(),
    val isLoading: Boolean = false,
)
