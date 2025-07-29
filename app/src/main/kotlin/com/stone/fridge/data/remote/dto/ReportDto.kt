package com.stone.fridge.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class ReportDto(
    val reportType: String,
    val content: String
)
