package com.stone.fridge.core.network.service

import com.stone.fridge.core.model.ChatMember
import com.stone.fridge.core.model.ChatRoom
import com.stone.fridge.core.model.ChatRoomRaw
import com.stone.fridge.core.model.Message
import com.stone.fridge.core.network.model.ApiResponse
import javax.inject.Inject

class ChatClient @Inject constructor(
    private val chatApi: ChatApi,
) {
    suspend fun readChats(roomId: Long): ApiResponse<Int> = chatApi.readChats(roomId)

    suspend fun joinChatRoom(roomId: Long): ApiResponse<ChatRoom> = chatApi.joinChatRoom(roomId)

    suspend fun closeChatRoom(roomId: Long): ApiResponse<String> = chatApi.closeChatRoom(roomId)

    suspend fun enterChatRoom(roomId: Long): ApiResponse<ChatRoom> = chatApi.enterChatRoom(roomId)

    suspend fun checkWhoIsIn(roomId: Long): ApiResponse<List<ChatMember>> = chatApi.checkWhoIsIn(roomId)

    suspend fun getMessages(roomId: Long, lastMessageId: Long? = null): ApiResponse<List<Message>> =
        chatApi.getMessages(roomId, lastMessageId, PAGING_SIZE)

    suspend fun getMyRooms(): ApiResponse<List<ChatRoomRaw>> = chatApi.getMyRooms()

    suspend fun exitChatRoom(roomId: Long): ApiResponse<String> = chatApi.exitChatRoom(roomId)

    companion object {
        private const val PAGING_SIZE = 20
    }
}