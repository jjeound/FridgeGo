package com.example.untitled_capstone.domain.model

import com.example.untitled_capstone.data.remote.dto.ContentDto
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
    fun toContentDto(): ContentDto = ContentDto(
        id = id,
        foodName = name,
        alarmStatus = notification,
        useByDate = expirationDate,
        storageType = isFridge,
        count = quantity.toInt(),
    )
    fun toNewFridgeItemDto(): NewFridgeItemDto = NewFridgeItemDto(
        alarmStatus = notification,
        count = quantity.toInt(),
        foodName = name,
        storageType = isFridge,
        useByDate = expirationDate
    )
}

