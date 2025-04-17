package com.example.untitled_capstone.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.untitled_capstone.domain.model.RecipeRaw

@Entity
data class RecipeItemEntity(
    @PrimaryKey(autoGenerate = true) val key: Long = 0,
    val id: Long,
    val title: String,
    val imageUrl: String?,
    val liked: Boolean,
    val pagerNumber: Int
){
    fun toRecipe(): RecipeRaw{
        return  RecipeRaw(
            id = id,
            title = title,
            imageUrl = imageUrl,
            liked = liked
        )
    }
}
