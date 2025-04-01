package com.example.untitled_capstone.data.remote.dto

data class AddressResponse(
    val isSuccess: Boolean,
    val code: String,
    val message: String,
    val result: LocationDto? = null
)
