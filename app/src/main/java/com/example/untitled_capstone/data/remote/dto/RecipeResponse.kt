package com.example.untitled_capstone.data.remote.dto


data class RecipeResponse(
    val code: String,
    val isSuccess: Boolean,
    val message: String,
    val result: RecipeResultDto? = null
)
