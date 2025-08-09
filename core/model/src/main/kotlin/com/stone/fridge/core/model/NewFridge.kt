package com.stone.fridge.core.model

import kotlinx.serialization.Serializable

@Serializable
data class NewFridge(
    val foodName: String,
    val count: Int,
    val useByDate: Long,
    val storageType: Boolean,
    val alarmStatus: Boolean,
)
