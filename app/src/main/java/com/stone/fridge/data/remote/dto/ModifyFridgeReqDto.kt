package com.stone.fridge.data.remote.dto

data class ModifyFridgeReqDto(
    val alarmStatus: Boolean,
    val count: Int,
    val foodName: String,
    val storageType: Boolean,
    val useByDate: Long,
    val imageUrl: String?
)
