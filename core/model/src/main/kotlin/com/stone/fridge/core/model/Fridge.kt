package com.stone.fridge.core.model

import kotlinx.serialization.Serializable

@Serializable
data class Fridge(
    val alarmStatus: Boolean,
    val count: Int,
    val imageUrl: String?,
    val foodName: String,
    val id: Long,
    val storageType: Boolean,
    val useByDate: Long
)
