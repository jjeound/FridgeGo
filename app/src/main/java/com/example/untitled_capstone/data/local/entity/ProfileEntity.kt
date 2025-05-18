package com.example.untitled_capstone.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.untitled_capstone.domain.model.Profile

@Entity
data class ProfileEntity(
    @PrimaryKey val id: Long,
    val email: String,
    val nickname: String? = "User",
    val imageUrl: String? = null,
    val trustLevelImageUrl: String? = null,
    val trustLevel: String? = null,
){
    fun toProfile(): Profile{
        return Profile(
            id = id,
            email = email,
            nickname = nickname,
            imageUrl = imageUrl,
            trustLevelImageUrl = trustLevelImageUrl,
            trustLevel = trustLevel
        )
    }
}
