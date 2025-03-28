package com.example.untitled_capstone.data.remote.dto

import com.example.untitled_capstone.data.local.entity.PostItemEntity

data class PostDto(
    val id: Long,
    val category: String,
    val content: String,
    val likeCount: Int,
    val memberCount: Int,
    val price: Int,
    val title: String
){
    fun toPostEntity(): PostItemEntity{
        return  PostItemEntity(
            id = id,
            category = category,
            content = content,
            likeCount = likeCount,
            memberCount = memberCount,
            price = price,
            title = title
        )
    }
}