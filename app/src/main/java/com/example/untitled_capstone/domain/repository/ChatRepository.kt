package com.example.untitled_capstone.domain.repository

import androidx.paging.PagingData
import com.example.untitled_capstone.core.util.Resource
import com.example.untitled_capstone.data.local.entity.MessageItemEntity
import com.example.untitled_capstone.domain.model.ChatMember
import com.example.untitled_capstone.domain.model.ChattingRoom
import com.example.untitled_capstone.domain.model.ChattingRoomRaw
import com.example.untitled_capstone.domain.model.Message
import kotlinx.coroutines.flow.Flow

interface ChatRepository {
    suspend fun readChats(id: Long): Resource<Int>
    suspend fun joinChatRoom(id: Long): Resource<ChattingRoom>
    suspend fun closeChatRoom(id: Long): Resource<String>
    suspend fun enterChatRoom(id: Long): Resource<ChattingRoom>
    suspend fun checkWhoIsIn(id: Long): Resource<List<ChatMember>>
    suspend fun getMessages(id: Long, lastMessageId: Long?): Resource<List<Message>>
    suspend fun getMyRooms(): Resource<List<ChattingRoomRaw>>
    suspend fun exitChatRoom(id: Long): Resource<String>
    fun getMessagePaged(roomId: Long): Flow<PagingData<MessageItemEntity>>
}