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
    fun readChats(id: Long): Flow<Resource<Int>>
    @WorkerThread
    fun joinChatRoom(id: Long): Flow<Resource<ChattingRoom>>
    @WorkerThread
    fun closeChatRoom(id: Long): Flow<Resource<String>>
    @WorkerThread
    fun enterChatRoom(id: Long): Flow<Resource<ChattingRoom>>
    @WorkerThread
    fun checkWhoIsIn(id: Long): Flow<Resource<List<ChatMember>>>
    @WorkerThread
    fun getMessages(id: Long, lastMessageId: Long?): Flow<Resource<List<Message>>>
    @WorkerThread
    fun getMyRooms(): Flow<Resource<List<ChattingRoomRaw>>>
    @WorkerThread
    fun exitChatRoom(id: Long): Flow<Resource<String>>
    @WorkerThread
    fun getMessagePaged(roomId: Long): Flow<PagingData<MessageItemEntity>>
}