package com.stone.fridge.data.remote.dto

data class ModifyRecipeBody(
    val title: String,
    val instructions: String,
    val ingredients: List<String>,
)
