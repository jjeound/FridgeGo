package com.stone.fridge.core.model

import kotlinx.serialization.Serializable

@Serializable
data class ModifyRecipeBody(
    val title: String,
    val instructions: String,
    val ingredients: List<String>,
)
