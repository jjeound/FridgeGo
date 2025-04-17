package com.example.untitled_capstone.data.remote.dto

data class ApiResponse<T>(
    val isSuccess: Boolean,
    val code: String,
    val message: String,
    val result: T? = null
)