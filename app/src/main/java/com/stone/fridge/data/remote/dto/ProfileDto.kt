package com.stone.fridge.data.remote.dto

import com.stone.fridge.domain.model.Profile
import kotlinx.serialization.Serializable

@Serializable
data class ProfileDto(
    val id: Long,
    val email: String,
    val nickname: String?,
    val imageUrl: String?,
    val trustLevelImageUrl: String,
    val trustLevel: String
) {
    fun toProfile(): Profile{
        return Profile(
            id = id,
            email = email,
            nickname = nickname,
            imageUrl = imageUrl,
            trustLevelImageUrl = trustLevelImageUrl,
            trustLevel = trustLevel
        )
    }
}