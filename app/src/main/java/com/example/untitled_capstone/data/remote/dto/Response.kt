package com.example.untitled_capstone.data.remote.dto

data class Response(
    val isSuccess: Int,
    val code: String,
    val message: String,
    val result: String
)
