package com.stone.fridge.core.model

import kotlinx.serialization.Serializable

@Serializable
data class NewFridgeItem(
    val alarmStatus: Boolean,
    val count: Int,
    val foodName: String,
    val storageType: Boolean,
    val useByDate: Long
)