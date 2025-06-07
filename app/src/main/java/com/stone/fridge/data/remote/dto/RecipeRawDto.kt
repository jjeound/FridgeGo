package com.stone.fridge.data.remote.dto

import com.stone.fridge.data.local.entity.RecipeItemEntity
import com.stone.fridge.domain.model.RecipeRaw
import kotlinx.serialization.Serializable

@Serializable
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
