package com.stone.fridge.core.model

import kotlinx.serialization.Serializable

@Serializable
data class ChatRoomRaw(
    val active: Boolean,
    val currentParticipants: Int,
    val lastMessage: String? = null,
    val lastMessageTime: String? = null,
    val name: String,
    val roomId: Long,
    val unreadCount: Int,
    val createdAt: String,
    val host: Boolean,
)