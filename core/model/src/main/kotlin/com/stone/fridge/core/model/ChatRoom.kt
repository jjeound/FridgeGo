package com.stone.fridge.core.model

import kotlinx.serialization.Serializable

@Serializable
data class ChatRoom(
    val active: Boolean,
    val currentParticipants: Int,
    val maxParticipants: Int,
    val name: String,
    val roomId: Long
)