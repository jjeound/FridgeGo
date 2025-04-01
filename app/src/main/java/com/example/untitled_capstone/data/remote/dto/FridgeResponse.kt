package com.example.untitled_capstone.data.remote.dto

data class FridgeResponse(
    val code: String,
    val isSuccess: Boolean,
    val message: String,
    val result: ContentDto? = null
)
