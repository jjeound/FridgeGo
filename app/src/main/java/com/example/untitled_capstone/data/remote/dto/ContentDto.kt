package com.example.untitled_capstone.data.remote.dto

import com.example.untitled_capstone.data.local.entity.FridgeItemEntity
import com.example.untitled_capstone.domain.model.FridgeItem

data class ContentDto(
    val alarmStatus: Boolean,
    val count: Int,
    val foodName: String,
    val id: Long,
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
    fun toFridgeItem(): FridgeItem {
        return  FridgeItem(
            id = id,
            name = foodName,
            image = null,
            quantity = count.toString(),
            expirationDate = useByDate,
            notification = alarmStatus,
            isFridge = storageType,
        )
    }
    fun toModifyFridgeReqDto(): ModifyFridgeReqDto{
        return ModifyFridgeReqDto(
            alarmStatus = alarmStatus,
            count = count,
            foodName = foodName,
            storageType = storageType,
            useByDate = useByDate
        )
    }
}