package com.example.untitled_capstone.data.remote.dto

data class AddPostResponse(
    val isSuccess: Boolean,
    val code: String,
    val message: String,
    val result: Long? = null
)
