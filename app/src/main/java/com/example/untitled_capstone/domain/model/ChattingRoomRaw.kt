package com.example.untitled_capstone.domain.model

import java.time.LocalDateTime


data class ChattingRoomRaw(
    val active: Boolean,
    val currentParticipants: Int,
    val lastMessage: String? = null,
    val lastMessageTime: LocalDateTime? = null,
    val name: String,
    val roomId: Long,
    val unreadCount: Int,
    val createdAt: LocalDateTime,
    val host: Boolean
)
