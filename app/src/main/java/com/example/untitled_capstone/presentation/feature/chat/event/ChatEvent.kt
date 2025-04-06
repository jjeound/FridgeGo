package com.example.untitled_capstone.presentation.feature.chat.event

sealed interface ChatEvent {
    data class CreateChatRoom(val name: String, val maxParticipants: Int): ChatEvent
    data class ReadChats(val id: Long): ChatEvent
    data class JoinChatRoom(val id: Long): ChatEvent
    data class CloseChatRoom(val id: Long): ChatEvent
    data class EnterChatRoom(val id: Long): ChatEvent
    data class CheckWhoIsIn(val id: Long): ChatEvent
    data class GetMessages(val id: Long): ChatEvent
    data object GetMyRooms: ChatEvent
    data class ExitChatRoom(val id: Long): ChatEvent
}