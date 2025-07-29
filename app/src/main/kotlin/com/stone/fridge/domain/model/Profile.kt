package com.stone.fridge.domain.model

data class Profile(
    val id: Long,
    val email: String,
    val nickname: String?,
    val imageUrl: String?,
    val trustLevelImageUrl: String?,
    val trustLevel: String?
)