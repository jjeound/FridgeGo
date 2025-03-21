package com.example.untitled_capstone.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class ProfileImage(
    val contentType: String,
    val createdAt: String,
    val fileSize: Int,
    val id: Int,
    val originalFilename: String,
    val updatedAt: String,
    val user: User,
    val uuid: String
)
