package com.stone.fridge.domain.model

import com.stone.fridge.data.remote.dto.NewFridgeItemDto
import kotlinx.serialization.Serializable

@Serializable
data class NewFridge(
    val foodName: String,
    val count: Int,
    val useByDate: Long,
    val storageType: Boolean,
    val alarmStatus: Boolean,
){
    fun toNewFridgeItemDto(): NewFridgeItemDto {
        return NewFridgeItemDto(
            foodName = foodName,
            count = count,
            useByDate = useByDate,
            storageType = storageType,
            alarmStatus = alarmStatus
        )
    }
}
