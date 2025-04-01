package com.example.untitled_capstone.domain.model

import java.time.LocalDateTime

data class Post(
    val category: String,
    val content: String,
    val createdAt: LocalDateTime,
    val district: String,
    val id: Long,
    val likeCount: Int,
    val memberCount: Int,
    val neighborhood: String,
    val nickname: String,
    val price: Int,
    val timeAgo: String,
    val title: String
)