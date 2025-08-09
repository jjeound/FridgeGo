package com.stone.fridge.core.data.chat

import androidx.annotation.WorkerThread
import androidx.paging.PagingData
import com.stone.fridge.core.model.ChatMember
import com.stone.fridge.core.model.ChatRoom
import com.stone.fridge.core.model.ChatRoomRaw
import com.stone.fridge.core.model.Message
import com.stone.fridge.core.model.UnreadBroadcast
import kotlinx.coroutines.flow.Flow

interface ChatRepository {
    @WorkerThread
    fun readChats(id: Long): Flow<Int>
    @WorkerThread
    fun joinChatRoom(id: Long): Flow<ChatRoom>
    @WorkerThread
    fun closeChatRoom(id: Long): Flow<String>
    @WorkerThread
    fun enterChatRoom(id: Long): Flow<ChatRoom>
    @WorkerThread
    fun checkWhoIsIn(id: Long): Flow<List<ChatMember>>
    @WorkerThread
    fun getMessages(id: Long, lastMessageId: Long?): Flow<List<Message>>
    @WorkerThread
    fun getMyRooms(): Flow<List<ChatRoomRaw>>
    @WorkerThread
    fun exitChatRoom(id: Long): Flow<String>
    @WorkerThread
    fun getMessagePaged(roomId: Long): Flow<PagingData<Message>>
    suspend fun connect(roomId: Long, onConnected: () -> Unit, onError: (Throwable) -> Unit)
    suspend fun subscribeRoom(
        roomId: Long,
        onUnreadUpdate: (UnreadBroadcast) -> Unit
    )
    fun sendMessage(roomId: Long, content: String)
    fun sendReadEvent(roomId: Long)
    fun leaveRoom(roomId: Long)
}