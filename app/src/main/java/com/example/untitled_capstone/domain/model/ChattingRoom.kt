package com.example.untitled_capstone.domain.model


data class ChattingRoom(
    val active: Boolean,
    val currentParticipants: Int,
    val maxParticipants: Int,
    val name: String,
    val roomId: Long
)
