package com.stone.fridge.data.remote.dto

import com.stone.fridge.domain.model.Post
import kotlinx.serialization.Serializable

@Serializable
data class PostDto(
    val category: String,
    val content: String,
    val createdAt: String? = null,
    val district: String,
    val id: Long,
    val likeCount: Int,
    val memberCount: Int,
    val neighborhood: String,
    val nickname: String,
    val price: Int,
    val timeAgo: String,
    val title: String,
    val image: List<ImageDto>? = null,
    val profileImageUrl: String? = null,
    val liked: Boolean,
    val chatRoomId: Long,
    val currentParticipants: Int,
    val roomActive: Boolean
){
    fun toPost(): Post{
        return Post(
            category = category,
            content = content,
            createdAt = createdAt,
            district = district,
            id = id,
            likeCount = likeCount,
            memberCount = memberCount,
            neighborhood = neighborhood,
            nickname = nickname,
            price = price,
            timeAgo = timeAgo,
            title = title,
            image = image?.map { it.toPostImage() },
            profileImageUrl = profileImageUrl,
            liked = liked,
            chatRoomId = chatRoomId,
            currentParticipants = currentParticipants,
            roomActive = roomActive
        )
    }
}