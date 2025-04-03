package com.example.untitled_capstone.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.untitled_capstone.domain.model.PostRaw

@Entity
data class PostItemEntity(
    @PrimaryKey val id: Long,
    val timeAgo: String,
    val likeCount: Int,
    val memberCount: Int,
    val price: Int,
    val title: String,
    val district: String,
    val neighborhood: String,
    val pagerNumber: Int
){
    fun toPostRaw(): PostRaw{
        return  PostRaw(
            id = id,
            timeAgo = timeAgo,
            district = district,
            neighborhood = neighborhood,
            likeCount = likeCount,
            memberCount = memberCount,
            price = price,
            title = title
        )
    }
}
