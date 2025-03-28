package com.example.untitled_capstone.data.remote.dto

data class NewPostDto(
    val category: String,
    val content: String,
    val memberCount: Int,
    val price: Int,
    val title: String
)