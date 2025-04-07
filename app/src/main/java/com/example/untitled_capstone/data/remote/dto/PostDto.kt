package com.example.untitled_capstone.data.remote.dto

import com.example.untitled_capstone.domain.model.Post

data class PostDto(
    val category: String,
    val content: String,
    val createdAt: String? = null,
    val district: String,
    val id: Long,
    val likeCount: Int,
    val memberCount: Int,
    val neighborhood: String,
    val nickname: String,
    val price: Int,
    val timeAgo: String,
    val title: String,
    val image: List<ImageDto>? = null,
    val profileImageUrl: String? = null,
    val liked: Boolean
){
    fun toPost(): Post{
        return Post(
            category = category,
            content = content,
            createdAt = createdAt,
            district = district,
            id = id,
            likeCount = likeCount,
            memberCount = memberCount,
            neighborhood = neighborhood,
            nickname = nickname,
            price = price,
            timeAgo = timeAgo,
            title = title,
            image = image?.map { it.toPostImage() },
            profileImageUrl = profileImageUrl,
            liked = liked
        )
    }
}