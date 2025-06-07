package com.stone.fridge.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class ModifyPostDto(
    val category: String,
    val content: String,
    val memberCount: Int?,
    val price: Int?,
    val title: String
)
