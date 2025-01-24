package com.example.untitled_capstone.feature.chatting.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class User(
    val id: Int,
    val profile: Int
)
