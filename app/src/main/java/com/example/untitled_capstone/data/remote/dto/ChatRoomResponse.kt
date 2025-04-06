package com.example.untitled_capstone.data.remote.dto

import com.example.untitled_capstone.domain.model.ChattingRoom


data class ChatRoomResponse(
    val active: Boolean,
    val currentParticipants: Int,
    val maxParticipants: Int,
    val name: String,
    val roomId: Long
){
    fun toChattingRoom(): ChattingRoom {
        return ChattingRoom(
            active = active,
            currentParticipants = currentParticipants,
            maxParticipants = maxParticipants,
            name = name,
            roomId = roomId
        )
    }
}