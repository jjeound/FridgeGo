package com.example.untitled_capstone.domain.model

import com.example.untitled_capstone.data.remote.dto.NewPostDto

data class NewPost(
    val category: String,
    val content: String,
    val memberCount: Int,
    val price: Int,
    val title: String
){
    fun toNewPostDto(): NewPostDto{
        return NewPostDto(
            category = category,
            content = content,
            memberCount = memberCount,
            price = price,
            title = title
        )
    }
}
