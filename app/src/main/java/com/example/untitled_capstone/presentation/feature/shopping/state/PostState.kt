package com.example.untitled_capstone.presentation.feature.shopping.state

import com.example.untitled_capstone.domain.model.Post

data class PostState(
    val posts: List<Post> = emptyList(),
    val loading: Boolean = false
)
