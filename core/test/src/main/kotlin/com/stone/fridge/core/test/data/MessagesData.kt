package com.stone.fridge.core.test.data

import com.stone.fridge.core.model.Message

val messagesData: List<Message> = listOf(
    Message(
        content = "안녕하세요! 반갑습니다.",
        senderId = 1L,
        senderNickname = "사용자1",
        sentAt = "2023-10-01T12:00:00Z",
        read = false,
        messageId = 1L,
        unreadCount = 1
    )
)