package com.example.untitled_capstone.domain.model

import com.example.untitled_capstone.data.remote.dto.ModifyFridgeReqDto
import com.example.untitled_capstone.data.remote.dto.NewFridgeItemDto

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
            useByDate = expirationDate
        )
    }
    fun toNewFridgeItemDto(): NewFridgeItemDto = NewFridgeItemDto(
        alarmStatus = notification,
        count = quantity.toInt(),
        foodName = name,
        storageType = isFridge,
        useByDate = expirationDate
    )
}

