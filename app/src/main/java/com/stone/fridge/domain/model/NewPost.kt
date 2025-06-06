package com.stone.fridge.domain.model

import com.stone.fridge.data.remote.dto.ModifyPostDto
import com.stone.fridge.data.remote.dto.NewPostDto

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
    fun toModifyPostDto(): ModifyPostDto {
        return ModifyPostDto(
            category = category,
            content = content,
            memberCount = memberCount,
            price = price,
            title = title
        )
    }
}
