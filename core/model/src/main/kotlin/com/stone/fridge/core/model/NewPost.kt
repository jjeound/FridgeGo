package com.stone.fridge.core.model

import kotlinx.serialization.Serializable

@Serializable
data class NewPost(
    val category: String,
    val content: String,
    val memberCount: Int,
    val price: Int,
    val title: String
)