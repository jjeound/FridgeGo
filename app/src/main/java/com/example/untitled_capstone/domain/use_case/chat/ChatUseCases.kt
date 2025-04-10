package com.example.untitled_capstone.domain.use_case.chat

data class ChatUseCases(
    val readChats: ChatRead,
    val joinChatRoom: ChatRoomJoin,
    val closeChatRoom: ChatRoomClose,
    val enterChatRoom: ChatRoomEnter,
    val checkWhoIsIn: ChatCheckWhoIsIn,
    val getMessages: ChatGetMessages,
    val getMyRooms: ChatGetMyRooms,
    val exitChatRoom: ChatRoomExit,
    val subscribeRoom: SubscribeRoom,
    val disconnect: Disconnect,
    val sendReadEvent: SendReadEvent,
    val sendMessage: SendMessage,
    val connectChatSocket: ConnectChatSocket,
    val getChatMessages: GetChatMessagesUseCase
)