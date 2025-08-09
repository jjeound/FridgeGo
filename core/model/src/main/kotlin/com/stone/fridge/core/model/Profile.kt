package com.stone.fridge.core.model

import kotlinx.serialization.Serializable

@Serializable
data class Profile(
    val id: Long,
    val email: String,
    val nickname: String?,
    val imageUrl: String?,
    val trustLevelImageUrl: String?,
    val trustLevel: String?
)