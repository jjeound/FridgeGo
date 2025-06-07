package com.stone.fridge.data.remote.dto

import com.stone.fridge.domain.model.PostImage
import kotlinx.serialization.Serializable

@Serializable
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
