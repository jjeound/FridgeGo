package com.stone.fridge.data.remote.dto

import com.stone.fridge.domain.model.Recipe

data class RecipeDto(
    val id: Long,
    val title: String,
    val instructions: String,
    val ingredients: List<String>,
    val imageUrl: String?,
    val liked: Boolean
){
    fun toRecipe(): Recipe{
        return Recipe(
            id = id,
            title = title,
            instructions = instructions,
            ingredients = ingredients,
            imageUrl = imageUrl,
            liked = liked
        )
    }
}
