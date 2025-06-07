package com.stone.fridge.data.remote.dto

import com.stone.fridge.data.local.entity.FridgeItemEntity
import com.stone.fridge.domain.model.FridgeItem
import kotlinx.serialization.Serializable

@Serializable
data class ContentDto(
    val alarmStatus: Boolean,
    val count: Int,
    val imageUrl: String?,
    val foodName: String,
    val id: Long,
    val storageType: Boolean,
    val useByDate: Long
){
    fun toFridgeItemEntity(pagerNumber: Int): FridgeItemEntity {
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
    fun toFridgeItem(): FridgeItem {
        return  FridgeItem(
            id = id,
            name = foodName,
            image = imageUrl,
            quantity = count.toString(),
            expirationDate = useByDate,
            notification = alarmStatus,
            isFridge = storageType,
        )
    }
}