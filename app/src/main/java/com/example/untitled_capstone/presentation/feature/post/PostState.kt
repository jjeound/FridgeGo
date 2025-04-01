package com.example.untitled_capstone.presentation.feature.post

import com.example.untitled_capstone.domain.model.Post

data class PostState(
    val post : Post? = null,
    val isLoading: Boolean = false,
    val error: String? = null
)