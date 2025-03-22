package com.example.untitled_capstone.data.remote.dto

import com.example.untitled_capstone.domain.model.Profile

data class ProfileDto(
    val email: String,
    val nickname: String?,
    val profileImage: ProfileImageDto?
) {
    fun toProfile(): Profile{
        return Profile(
            email = email,
            nickname = nickname,
            profileImage = profileImage?.toProfileImage()
        )
    }
}