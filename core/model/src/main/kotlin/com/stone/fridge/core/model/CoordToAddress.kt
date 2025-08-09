package com.stone.fridge.core.model

import kotlinx.serialization.Serializable

@Serializable
data class CoordToAddress(
    val meta: Meta,
    val documents: List<Documents>? = null
)
