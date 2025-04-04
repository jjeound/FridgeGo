package com.example.untitled_capstone.domain.model

data class RecipeRaw(
    val id: Long,
    val title: String,
    val imageUrl: String?,
    val liked: Boolean,
)
