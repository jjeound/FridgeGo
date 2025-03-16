package com.example.untitled_capstone.data.remote.dto

import com.example.untitled_capstone.data.local.entity.FridgeItemEntity

data class ContentDto(
    val alarmStatus: Boolean,
    val count: Int,
    val foodName: String,
    val id: Int,
    val storageType: Boolean,
    val useByDate: Long
){
    fun toFridgeItemEntity(): FridgeItemEntity {
        return FridgeItemEntity(
            id = id,
            name = foodName,
            image = null,
            quantity = count.toString(),
            expirationDate = useByDate,
            notification = alarmStatus,
            isFridge = storageType,
        )
    }
}