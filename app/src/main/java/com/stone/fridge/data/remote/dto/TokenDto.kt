package com.stone.fridge.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class TokenDto(
    val accessToken: String,
    val refreshToken: String,
)
