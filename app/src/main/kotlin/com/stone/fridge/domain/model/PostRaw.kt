package com.stone.fridge.domain.model

data class PostRaw(
    val id: Long,
    val timeAgo: String,
    val likeCount: Int,
    val memberCount: Int,
    val price: Int,
    val title: String,
    val district: String,
    val neighborhood: String,
    val imageUrls: List<String>,
    val liked: Boolean,
    val currentParticipants: Int,
    val roomActive: Boolean
)
