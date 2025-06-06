package com.stone.fridge.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Post(
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
    val image: List<PostImage>? = null,
    val profileImageUrl: String? = null,
    val liked: Boolean,
    val chatRoomId: Long,
    val currentParticipants: Int,
    val roomActive: Boolean
): Parcelable