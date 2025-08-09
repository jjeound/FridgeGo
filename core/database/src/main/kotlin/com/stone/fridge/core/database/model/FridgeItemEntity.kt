package com.stone.fridge.core.database.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.stone.fridge.core.model.Fridge

@Entity
data class FridgeItemEntity(
    @PrimaryKey(autoGenerate = true) val key: Long = 0,
    val id: Long,
    val name: String,
    val image: String?,
    val quantity: String,
    val expirationDate: Long,
    var notification: Boolean,
    val isFridge: Boolean,
    val pagerNumber: Int
)

fun FridgeItemEntity.toDomain(): Fridge{
    return Fridge(
        id = id,
        foodName = name,
        imageUrl = image,
        count = quantity.toIntOrNull() ?: 0,
        useByDate = expirationDate,
        alarmStatus = notification,
        storageType = isFridge
    )
}

fun Fridge.toEntity(pagerNumber: Int = 0): FridgeItemEntity {
    return FridgeItemEntity(
        id = id,
        name = foodName,
        image = imageUrl,
        quantity = count.toString(),
        expirationDate = useByDate,
        notification = alarmStatus,
        isFridge = storageType,
        pagerNumber = pagerNumber
    )
}
