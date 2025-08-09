package com.stone.fridge.core.model

import kotlinx.serialization.Serializable

@Serializable
data class Preference(
    val tastePreference: String
)
