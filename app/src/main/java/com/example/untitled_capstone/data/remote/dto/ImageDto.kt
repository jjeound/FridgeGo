package com.example.untitled_capstone.data.remote.dto

import com.example.untitled_capstone.domain.use_case.post.PostImage

data class ImageDto(
    val imageUrl: String,
    val imageId: Long
){
    fun toPostImage(): PostImage{
        return PostImage(
            imageUrl = imageUrl,
            id = imageId
        )
    }
}
