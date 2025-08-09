package com.stone.fridge.core.model

import kotlinx.serialization.Serializable

@Serializable
data class Report(
    val reportType: String,
    val content: String
)
