package com.example.untitled_capstone.data.remote.dto

import com.example.untitled_capstone.data.local.entity.PostItemEntity

data class PostDto(
    val id: Long,
    val category: String,
    val content: String,
    val like_count: Int,
    val price: Int,
    val title: String
){
    fun toPostEntity(): PostItemEntity{
        return  PostItemEntity(
            id = id,
            category = category,
            content = content,
            like_count = like_count,
            price = price,
            title = title
        )
    }
}