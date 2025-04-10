package com.example.untitled_capstone.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.example.untitled_capstone.data.util.Converters
import com.example.untitled_capstone.domain.model.PostRaw

@Entity
@TypeConverters(Converters::class)
data class PostItemEntity(
    @PrimaryKey val id: Long,
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
            title = title,
            imageUrls = imageUrls,
            liked = liked,
            currentParticipants = currentParticipants,
            roomActive = roomActive
        )
    }
}
