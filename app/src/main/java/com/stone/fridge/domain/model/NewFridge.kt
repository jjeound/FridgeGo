package com.stone.fridge.domain.model

data class NewFridge(
    val foodName: String,
    val count: Int,
    val useByDate: Long,
    val storageType: Boolean,
    val alarmStatus: Boolean,
)
