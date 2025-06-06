package com.stone.fridge.domain.model


data class ChattingRoom(
    val active: Boolean,
    val currentParticipants: Int,
    val maxParticipants: Int,
    val name: String,
    val roomId: Long
)
