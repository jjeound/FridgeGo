package com.stone.fridge.core.model

import kotlinx.serialization.Serializable

@Serializable
data class Token(
    val accessToken: String,
    val refreshToken: String,
)
