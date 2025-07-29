package com.stone.fridge.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class Recipe(
    val id: Long,
    val title: String,
    val instructions: String,
    val ingredients: List<String>,
    val imageUrl: String? = null,
    val liked: Boolean
)