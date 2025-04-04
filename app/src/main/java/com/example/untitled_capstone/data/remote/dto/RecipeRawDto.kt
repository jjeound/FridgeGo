package com.example.untitled_capstone.data.remote.dto

import com.example.untitled_capstone.data.local.entity.RecipeItemEntity
import com.example.untitled_capstone.domain.model.RecipeRaw


data class RecipeRawDto(
    val id: Long,
    val title: String,
    val imageUrl: String?,
    val liked: Boolean
){
    fun toRecipeEntity(pagerNumber: Int): RecipeItemEntity{
        return RecipeItemEntity(
            id = id,
            title = title,
            imageUrl = imageUrl,
            liked = liked,
            pagerNumber = pagerNumber
        )
    }
    fun toRecipeRaw(): RecipeRaw{
        return RecipeRaw(
            id = id,
            title = title,
            imageUrl = imageUrl,
            liked = liked
        )
    }
}
