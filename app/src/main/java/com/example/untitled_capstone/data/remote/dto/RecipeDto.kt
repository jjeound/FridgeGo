package com.example.untitled_capstone.data.remote.dto

import com.example.untitled_capstone.data.local.entity.RecipeItemEntity
import com.example.untitled_capstone.domain.model.RecipeRaw


data class RecipeDto(
    val id: Long,
    val title: String,
    val instructions: String,
    val liked: Boolean
){
    fun toRecipeEntity(pagerNumber: Int): RecipeItemEntity{
        return RecipeItemEntity(
            id = id,
            title = title,
            instructions = instructions,
            liked = liked,
            pagerNumber = pagerNumber
        )
    }
    fun toRecipe(): RecipeRaw{
        return RecipeRaw(
            id = id,
            title = title,
            instructions = instructions,
            liked = liked
        )
    }
}
