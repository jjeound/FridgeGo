package com.example.untitled_capstone.data.remote.dto

data class ChatbotResponse(
    val code: String,
    val isSuccess: Boolean,
    val message: String,
    val result: ChatbotResultDto
)
