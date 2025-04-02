package com.example.untitled_capstone.data.remote.dto

data class PostLikedResponse(
    val code: String,
    val isSuccess: Boolean,
    val message: String,
    val result: PostLikedDto? = null
)
