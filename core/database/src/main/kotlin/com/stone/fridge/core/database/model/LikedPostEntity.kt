package com.stone.fridge.core.database.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.stone.fridge.core.model.PostRaw

@Entity
data class LikedPostEntity(
    @PrimaryKey(autoGenerate = true) val key: Long = 0,
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
    val pagerNumber: Int,
    val currentParticipants: Int,
    val roomActive: Boolean
)

fun LikedPostEntity.toDomain(): PostRaw {
    return PostRaw(
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
        currentParticipants = currentParticipants,
        roomActive = roomActive
    )
}

fun PostRaw.toLikedEntity(pagerNumber: Int = 0): LikedPostEntity {
    return LikedPostEntity(
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
        currentParticipants = currentParticipants,
        roomActive = roomActive,
        pagerNumber = pagerNumber
    )
}