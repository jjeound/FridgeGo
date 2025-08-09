package com.stone.fridge.core.model

import kotlinx.serialization.Serializable

@Serializable
data class ChatMember(
    val userId: Long,
    val host: Boolean,
    val imageUrl: String? = null,
    val nickname: String
)