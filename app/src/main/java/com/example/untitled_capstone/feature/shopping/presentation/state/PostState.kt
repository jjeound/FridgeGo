package com.example.untitled_capstone.feature.shopping.presentation.state

import com.example.untitled_capstone.feature.shopping.domain.model.Post

data class PostState(
    val posts: List<Post> = emptyList(),
    val loading: Boolean = false
)
