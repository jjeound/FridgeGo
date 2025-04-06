package com.example.untitled_capstone.data.remote.dto

data class ApiResponseTest<T>(
    val isSuccess: Boolean,
    val code: String,
    val message: String,
    val result: T? = null
)