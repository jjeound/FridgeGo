package com.example.untitled_capstone.domain.model

import com.example.untitled_capstone.data.remote.dto.PostDto

data class NewPost(
    val category: String,
    val content: String,
    val like_count: Int,
    val price: Int,
    val title: String
){
    fun toPostDto(): PostDto{
        return PostDto(
            category = category,
            content = content,
            like_count = like_count,
            price = price,
            title = title
        )
    }
}
