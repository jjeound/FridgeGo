package com.example.untitled_capstone.domain.model


data class ChattingRoomRaw(
    val active: Boolean,
    val currentParticipants: Int,
    val lastMessage: String? = null,
    val lastMessageTime: String? = null,
    val name: String,
    val roomId: Long,
    val unreadCount: Int
)
