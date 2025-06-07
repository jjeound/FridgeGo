package com.stone.fridge.domain.model

import com.stone.fridge.data.remote.dto.ModifyFridgeReqDto
import com.stone.fridge.data.remote.dto.NewFridgeItemDto
import kotlinx.serialization.Serializable

@Serializable
data class FridgeItem(
    val id: Long,
    val name: String,
    val image: String?,
    val quantity: String,
    val expirationDate: Long,
    var notification: Boolean,
    val isFridge: Boolean
){
    fun toModifyFridgeReqDto(): ModifyFridgeReqDto{
        return ModifyFridgeReqDto(
            alarmStatus = notification,
            count = quantity.toInt(),
            foodName = name,
            storageType = isFridge,
            useByDate = expirationDate,
            imageUrl = image
        )
    }
}

