package com.stone.fridge.core.database.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.stone.fridge.core.model.RecipeRaw

@Entity
data class RecipeItemEntity(
    @PrimaryKey(autoGenerate = true) val key: Long = 0,
    val id: Long,
    val title: String,
    val imageUrl: String?,
    val liked: Boolean,
    val pagerNumber: Int
)

fun RecipeRaw.toEntity(pagerNumber: Int = 0): RecipeItemEntity {
    return RecipeItemEntity(
        id = id,
        title = title,
        imageUrl = imageUrl,
        liked = liked,
        pagerNumber = pagerNumber
    )
}

fun RecipeItemEntity.toDomain(): RecipeRaw {
    return RecipeRaw(
        id = id,
        title = title,
        imageUrl = imageUrl,
        liked = liked
    )
}
