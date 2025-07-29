package com.stone.fridge.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class EmailReq(
    val email: String
)
