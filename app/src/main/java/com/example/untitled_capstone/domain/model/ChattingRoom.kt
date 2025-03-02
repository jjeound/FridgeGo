package com.example.untitled_capstone.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class ChattingRoom(
    val message: String,
    val lastSentMessageTime: String,
    val user: User,
    val title: String,
    val numberOfPeople: Int,
    val messagesNotReadYet: Int = 0
)
