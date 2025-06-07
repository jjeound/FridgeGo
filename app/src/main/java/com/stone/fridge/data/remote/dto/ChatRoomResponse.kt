package com.stone.fridge.data.remote.dto

import com.stone.fridge.domain.model.ChattingRoom
import kotlinx.serialization.Serializable

@Serializable
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