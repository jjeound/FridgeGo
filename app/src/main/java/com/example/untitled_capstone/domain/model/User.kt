package com.example.untitled_capstone.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class User(
    val id: Int,
    val name: String,
    val profile: Int?
)
