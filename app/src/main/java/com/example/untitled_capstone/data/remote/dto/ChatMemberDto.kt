package com.example.untitled_capstone.data.remote.dto

import com.example.untitled_capstone.domain.model.ChatMember

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