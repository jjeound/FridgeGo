package com.stone.fridge.core.model

import kotlinx.serialization.Serializable

@Serializable
data class Location(
    val district: String,
    val neighborhood: String
)
