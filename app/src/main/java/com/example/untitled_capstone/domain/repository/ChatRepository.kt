package com.example.untitled_capstone.domain.repository

import androidx.annotation.WorkerThread
import androidx.paging.PagingData
import com.example.untitled_capstone.core.util.Resource
import com.example.untitled_capstone.data.local.entity.MessageItemEntity
import com.example.untitled_capstone.domain.model.ChatMember
import com.example.untitled_capstone.domain.model.ChattingRoom
import com.example.untitled_capstone.domain.model.ChattingRoomRaw
import com.example.untitled_capstone.domain.model.Message
import kotlinx.coroutines.flow.Flow

interface ChatRepository {
    @WorkerThread
    suspend fun readChats(id: Long): Flow<Resource<Int>>
    @WorkerThread
    suspend fun joinChatRoom(id: Long): Flow<Resource<ChattingRoom>>
    @WorkerThread
    suspend fun closeChatRoom(id: Long): Flow<Resource<String>>
    @WorkerThread
    suspend fun enterChatRoom(id: Long): Flow<Resource<ChattingRoom>>
    @WorkerThread
    suspend fun checkWhoIsIn(id: Long): Flow<Resource<List<ChatMember>>>
    @WorkerThread
    suspend fun getMessages(id: Long, lastMessageId: Long?): Flow<Resource<List<Message>>>
    @WorkerThread
    suspend fun getMyRooms(): Flow<Resource<List<ChattingRoomRaw>>>
    @WorkerThread
    suspend fun exitChatRoom(id: Long): Flow<Resource<String>>
    @WorkerThread
    fun getMessagePaged(roomId: Long): Flow<PagingData<MessageItemEntity>>
}