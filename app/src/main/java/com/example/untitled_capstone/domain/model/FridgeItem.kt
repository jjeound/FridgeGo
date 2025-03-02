package com.example.untitled_capstone.domain.model

data class FridgeItem(
    val id: Int,
    val name: String,
    val image: Int?,
    val quantity: String,
    val expirationDate: Long,
    var notification: Boolean,
    val isFridge: Boolean
)
