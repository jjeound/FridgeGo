package com.example.untitled_capstone.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.untitled_capstone.domain.model.PostRaw
import com.example.untitled_capstone.domain.model.RecipeRaw

@Entity
data class PostItemEntity(
    @PrimaryKey val id: Long,
    val category: String,
    val content: String,
    val like_count: Int,
    val price: Int,
    val title: String
){
    fun toPostRaw(): PostRaw{
        return  PostRaw(
            id = id,
            category = category,
            content = content,
            like_count = like_count,
            price = price,
            title = title
        )
    }
}
