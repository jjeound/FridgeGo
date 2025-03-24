package com.example.untitled_capstone.data.remote.dto

data class PostDto(
    val category: String,
    val content: String,
    val like_count: Int,
    val price: Int,
    val title: String
)