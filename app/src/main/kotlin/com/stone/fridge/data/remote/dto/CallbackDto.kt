package com.stone.fridge.data.remote.dto

import com.stone.fridge.domain.model.AccountInfo
import kotlinx.serialization.Serializable

@Serializable
data class CallbackDto(
    val id: Long,
    val email: String,
    val accessToken: String,
    val refreshToken: String,
    val nickname: String?,
){
    fun toAccountInfo(): AccountInfo{
        return AccountInfo(
            id = id,
            email = email,
            nickname = nickname
        )
    }
}