package com.example.untitled_capstone.feature.refrigerator.domain.model

data class FridgeItem(
    val id: Int,
    val name: String,
    val image: Int?,
    val quantity: Int,
    val expirationDate: Long,
    var notification: Boolean,
    val isFridge: Boolean
)
