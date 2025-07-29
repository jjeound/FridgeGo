package com.stone.fridge.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class Ingredient(
    val alarmStatus: String,
    val count: Int,
    val createdAt: String,
    val foodName: String,
    val id: Int,
    val storageType: String,
    val updatedAt: String,
    val useByDate: String,
    val user: String
)