package com.example.untitled_capstone.data.remote.dto

import com.example.untitled_capstone.domain.model.AccountInfo

data class CallbackDto(
    val id: Int,
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