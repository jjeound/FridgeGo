package com.stone.fridge.data.remote.dto

data class ModifyPostDto(
    val category: String,
    val content: String,
    val memberCount: Int?,
    val price: Int?,
    val title: String
)
