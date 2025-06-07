package com.stone.fridge.data.remote.dto

import com.stone.fridge.domain.model.ChatMember
import kotlinx.serialization.Serializable

@Serializable
data class ChatMemberDto(
    val host: Boolean,
    val imageUrl: String? = null,
    val nickname: String
){
    fun toChatMember(): ChatMember {
        return ChatMember(
            host = host,
            imageUrl = imageUrl,
            nickname = nickname
        )
    }
}