package com.stone.fridge.domain.model

data class ChatMember(
    val userId: Long,
    val host: Boolean,
    val imageUrl: String? = null,
    val nickname: String
)
