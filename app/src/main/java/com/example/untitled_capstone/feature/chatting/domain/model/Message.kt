package com.example.untitled_capstone.feature.chatting.domain.model

data class Message(
    val message: String,
    val time: String,
    val user: User,
)