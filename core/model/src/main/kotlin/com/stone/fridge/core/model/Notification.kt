package com.stone.fridge.core.model

import kotlinx.serialization.Serializable

@Serializable
data class Notification(
    val id: Long,
    val ingredientName: String,
    val content: String?,
    val scheduledAt: String,
    val status: String,
    val read: Boolean
)