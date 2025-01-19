package com.example.untitled_capstone.feature.home.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class Recipe(
    val title: String,
    val image: Int?,
    val ingredients: List<String>,
    val steps: List<String>,
    val isLiked: Boolean
)