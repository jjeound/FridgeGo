package com.stone.fridge.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class PostLikedDto(
    val liked: Boolean
)
