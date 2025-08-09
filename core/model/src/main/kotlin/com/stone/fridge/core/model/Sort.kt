package com.stone.fridge.core.model

import kotlinx.serialization.Serializable

@Serializable
data class Sort(
    val direction: String,
    val nullHandling: String,
    val ascending: Boolean,
    val property: String,
    val ignoreCase: Boolean
)