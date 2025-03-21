package com.example.untitled_capstone.domain.model

import kotlinx.serialization.Serializable


@Serializable
data class Profile(
    val email: String,
    val nickname: String,
    val profileImage: ProfileImage
)