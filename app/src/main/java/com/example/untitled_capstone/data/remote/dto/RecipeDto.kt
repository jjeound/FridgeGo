package com.example.untitled_capstone.data.remote.dto

import com.example.untitled_capstone.data.local.entity.RecipeItemEntity


data class RecipeDto(
    val id: Long,
    val title: String,
    val instructions: String
){
    fun toRecipeEntity(): RecipeItemEntity{
        return RecipeItemEntity(
            id = id,
            title = title,
            instructions = instructions
        )
    }
}
