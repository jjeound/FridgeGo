package com.example.untitled_capstone.data.remote.dto

data class CallbackResultDto(
    val accessToken: String,
    val email: String,
    val id: Int,
    val nickname: String,
    val refreshToken: String
)