package com.stone.fridge.core.model

import kotlinx.serialization.Serializable

@Serializable
data class Callback(
    val id: Long,
    val email: String,
    val accessToken: String,
    val refreshToken: String,
    val nickname: String?,
)

fun Callback.toAccountInfo(): AccountInfo {
    return AccountInfo(
        id = id,
        email = email,
        nickname = nickname
    )
}