package com.stone.fridge.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class ModifyRecipeBody(
    val title: String,
    val instructions: String,
    val ingredients: List<String>,
)
