package com.example.untitled_capstone.data.remote.dto

import com.example.untitled_capstone.domain.model.ProfileImage

data class ProfileImageDto(
    val contentType: String,
    val createdAt: String,
    val updatedAt: String,
    val fileSize: Int,
    val id: Int,
    val originalFilename: String,
    val user: UserDto,
    val uuid: String
){
    fun toProfileImage(): ProfileImage{
        return ProfileImage(
            contentType = contentType,
            createdAt = createdAt,
            fileSize = fileSize,
            id = id,
            originalFilename = originalFilename,
            updatedAt = updatedAt,
            user = user.toUser(),
            uuid = uuid
        )
    }
}