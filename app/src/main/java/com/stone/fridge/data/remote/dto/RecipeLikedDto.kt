package com.stone.fridge.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class RecipeLikedDto(
    val liked: Boolean
)