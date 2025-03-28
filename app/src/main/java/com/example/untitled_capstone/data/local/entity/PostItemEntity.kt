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
    val likeCount: Int,
    val memberCount: Int,
    val price: Int,
    val title: String
){
    fun toPostRaw(): PostRaw{
        return  PostRaw(
            id = id,
            category = category,
            content = content,
            likeCount = likeCount,
            memberCount = memberCount,
            price = price,
            title = title
        )
    }
}
