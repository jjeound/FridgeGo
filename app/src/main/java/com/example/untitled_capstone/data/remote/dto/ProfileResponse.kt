package com.example.untitled_capstone.data.remote.dto

data class ProfileResponse(
    val isSuccess: Boolean,
    val code: String,
    val message: String,
    val result: ProfileDto
)
