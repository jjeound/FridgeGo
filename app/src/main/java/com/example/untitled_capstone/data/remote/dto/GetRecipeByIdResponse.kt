package com.example.untitled_capstone.data.remote.dto

data class GetRecipeByIdResponse(
    val code: String,
    val isSuccess: Boolean,
    val message: String,
    val result: RecipeDto? = null
)
