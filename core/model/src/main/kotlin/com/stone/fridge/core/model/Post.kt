package com.stone.fridge.core.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

@Parcelize
@Serializable
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
    val image: List<Image>? = null,
    val profileImageUrl: String? = null,
    val liked: Boolean,
    val chatRoomId: Long,
    val currentParticipants: Int,
    val roomActive: Boolean
) : Parcelable