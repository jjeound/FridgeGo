package com.example.untitled_capstone.presentation.feature.post

data class UploadState(
    val id : Long? = null,
    val isSuccess: Boolean = false,
    val isLoading: Boolean = false,
    val error: String? = null
)