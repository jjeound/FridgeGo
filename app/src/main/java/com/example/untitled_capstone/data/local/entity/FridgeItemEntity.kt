package com.example.untitled_capstone.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.untitled_capstone.domain.model.FridgeItem

@Entity
data class FridgeItemEntity(
    @PrimaryKey val id: Long,
    val name: String,
    val image: String?,
    val quantity: String,
    val expirationDate: Long,
    var notification: Boolean,
    val isFridge: Boolean,
    val pagerNumber: Int
){
    fun toFridgeItem(): FridgeItem{
        return FridgeItem(
            id = id,
            name = name,
            image = image,
            quantity = quantity,
            expirationDate = expirationDate,
            notification = notification,
            isFridge = isFridge
        )
    }
}
