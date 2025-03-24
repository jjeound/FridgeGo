package com.example.untitled_capstone.data.local.entity

import androidx.room.PrimaryKey
import com.example.untitled_capstone.domain.model.RecipeRaw

data class RecipeItemEntity(
    @PrimaryKey val id: Long,
    val title: String,
    val instructions: String
){
    fun toRecipe(): RecipeRaw{
        return  RecipeRaw(
            id = id,
            title = title,
            instructions = instructions
        )
    }
}
