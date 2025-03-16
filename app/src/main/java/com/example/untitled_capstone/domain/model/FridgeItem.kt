package com.example.untitled_capstone.domain.model

import com.example.untitled_capstone.data.remote.dto.ContentDto
import kotlinx.serialization.Serializable

@Serializable
data class FridgeItem(
    val id: Int,
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
}

