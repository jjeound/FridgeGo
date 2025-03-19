package com.example.untitled_capstone.data.remote.dto

data class CallbackDto(
    val id: Int,
    val email: String,
    val accessToken: String,
    val refreshToken: String,
    val nickname: String,
)