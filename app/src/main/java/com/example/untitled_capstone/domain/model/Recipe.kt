package com.example.untitled_capstone.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class Recipe(
    val id: Int,
    val title: String,
    val image: String?,
    val ingredients: List<String>,
    val steps: List<String>,
    val isLiked: Boolean
)