package com.stone.fridge.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class LocationDto(
    val district: String,
    val neighborhood: String
)
