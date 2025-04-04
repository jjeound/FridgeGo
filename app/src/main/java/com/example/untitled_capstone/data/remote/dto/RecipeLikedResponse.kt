package com.example.untitled_capstone.data.remote.dto

data class RecipeLikedResponse(
    val isSuccess: Boolean,
    val code: String,
    val message: String,
    val result: RecipeLikedDto? = null
)
