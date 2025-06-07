package com.stone.fridge.data.remote.dto

import com.stone.fridge.data.local.entity.LikedPostEntity
import com.stone.fridge.data.local.entity.MyPostEntity
import com.stone.fridge.data.local.entity.PostItemEntity
import kotlinx.serialization.Serializable
import kotlin.Int

@Serializable
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
    val liked: Boolean,
    val currentParticipants: Int,
    val roomActive: Boolean
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
            pagerNumber = pagerNumber,
            currentParticipants = currentParticipants,
            roomActive = roomActive
        )
    }
    fun toLikedPostEntity(pagerNumber: Int): LikedPostEntity{
        return  LikedPostEntity(
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
            pagerNumber = pagerNumber,
            currentParticipants = currentParticipants,
            roomActive = roomActive
        )
    }
    fun toMyPostEntity(pagerNumber: Int): MyPostEntity{
        return  MyPostEntity(
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
            pagerNumber = pagerNumber,
            currentParticipants = currentParticipants,
            roomActive = roomActive
        )
    }
}