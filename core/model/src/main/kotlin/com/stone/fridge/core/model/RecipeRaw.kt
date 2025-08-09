package com.stone.fridge.core.model

import kotlinx.serialization.Serializable

@Serializable
data class RecipeRaw(
    val id: Long,
    val title: String,
    val imageUrl: String?,
    val liked: Boolean
)