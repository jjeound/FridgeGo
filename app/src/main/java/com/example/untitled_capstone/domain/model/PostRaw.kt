package com.example.untitled_capstone.domain.model

data class PostRaw(
    val id: Long,
    val category: String,
    val content: String,
    val like_count: Int,
    val price: Int,
    val title: String
)
