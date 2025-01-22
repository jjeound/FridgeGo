package com.example.untitled_capstone.feature.refrigerator.domain.model

data class FridgeItem(
    val name: String,
    val image: Int?,
    val quantity: Int,
    val expirationDate: String,
    val notification: Boolean
)
