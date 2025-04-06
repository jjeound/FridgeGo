package com.example.untitled_capstone.data.remote.dto

import com.example.untitled_capstone.data.local.entity.PostItemEntity
import kotlin.Int

data class PostRawDto(
    val id: Long,
    val timeAgo: String,
    val likeCount: Int,
    val memberCount: Int,
    val price: Int,
    val title: String,
    val district: String,
    val neighborhood: String,
    val imageUrls: List<String>,
    val liked: Boolean
){
    fun toPostEntity(pagerNumber: Int): PostItemEntity{
        return  PostItemEntity(
            id = id,
            timeAgo = timeAgo,
            district = district,
            neighborhood = neighborhood,
            likeCount = likeCount,
            memberCount = memberCount,
            price = price,
            title = title,
            imageUrls = imageUrls,
            liked = liked,
            pagerNumber = pagerNumber
        )
    }
}