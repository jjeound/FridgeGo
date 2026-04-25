package com.stone.fridge.core.test.data

import com.stone.fridge.core.model.ChatRoom


val chattingRoomsData: List<ChatRoom> = listOf(
  ChatRoom(
      roomId = 0L,
      name = "채팅방 1",
      currentParticipants = 1,
      maxParticipants = 10,
      active = true
  ),
)