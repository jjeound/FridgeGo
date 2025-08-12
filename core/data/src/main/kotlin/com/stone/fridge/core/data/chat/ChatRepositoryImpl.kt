package com.stone.fridge.core.data.chat

import androidx.annotation.WorkerThread
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.stone.fridge.core.auth.TokenDataSource
import com.stone.fridge.core.database.dao.MessageDao
import com.stone.fridge.core.database.model.toDomain
import com.stone.fridge.core.database.model.toEntity
import com.stone.fridge.core.model.ChatMember
import com.stone.fridge.core.model.ChatRoom
import com.stone.fridge.core.model.ChatRoomRaw
import com.stone.fridge.core.model.Message
import com.stone.fridge.core.model.UnreadBroadcast
import com.stone.fridge.core.network.AppDispatchers
import com.stone.fridge.core.network.Dispatcher
import com.stone.fridge.core.network.manager.WebSocketManager
import com.stone.fridge.core.network.service.ChatClient
import com.stone.fridge.core.paging.MessagePagingSource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ChatRepositoryImpl @Inject constructor(
    private val chatClient: ChatClient,
    private val dao: MessageDao,
    private val mediatorFactory: MessagePagingSource.Factory,
    private val webSocketManager: WebSocketManager,
    private val tokenDataSource: TokenDataSource,
    @param:Dispatcher(AppDispatchers.IO) private val ioDispatcher: CoroutineDispatcher,
): ChatRepository {
    val scope = CoroutineScope(SupervisorJob() + ioDispatcher)

    @WorkerThread
    override fun readChats(id: Long): Flow<Int> = flow {
        chatClient.readChats(id).result?.let {
            emit(it)
        }
    }.flowOn(ioDispatcher)

    @WorkerThread
    override fun joinChatRoom(id: Long): Flow<ChatRoom> = flow {
        chatClient.joinChatRoom(id).result?.let {
            emit(it)
        }
    }.flowOn(ioDispatcher)

    @WorkerThread
    override fun closeChatRoom(id: Long): Flow<String> = flow {
        chatClient.closeChatRoom(id).result?.let {
            emit(it)
        }
    }.flowOn(ioDispatcher)

    @WorkerThread
    override fun enterChatRoom(id: Long): Flow<ChatRoom> = flow {
        chatClient.enterChatRoom(id).result?.let {
            emit(it)
        }
    }.flowOn(ioDispatcher)

    @WorkerThread
    override fun checkWhoIsIn(id: Long): Flow<List<ChatMember>> = flow {
        chatClient.checkWhoIsIn(id).result?.let {
            emit(it)
        }
    }.flowOn(ioDispatcher)

    @WorkerThread
    override fun getMessages(id: Long, lastMessageId: Long?): Flow<List<Message>> = flow {
        chatClient.getMessages(id, lastMessageId).result?.let {
            emit(it)
        }
    }.flowOn(ioDispatcher)

    @WorkerThread
    override fun getMyRooms(): Flow<List<ChatRoomRaw>> = flow {
        chatClient.getMyRooms().result?.let {
            emit(it)
        }
    }.flowOn(ioDispatcher)

    @WorkerThread
    override fun exitChatRoom(id: Long): Flow<String> = flow {
        chatClient.exitChatRoom(id).result?.let {
            dao.clearMessages(id)
            emit(it)
        }
    }.flowOn(ioDispatcher)

    @OptIn(ExperimentalPagingApi::class)
    @WorkerThread
    override fun getMessagePaged(roomId: Long): Flow<PagingData<Message>> {
        return Pager(
            config = PagingConfig(pageSize = 20),
            remoteMediator = mediatorFactory.create(roomId),
            pagingSourceFactory = {
                dao.getMessagesPaging(roomId)
            }
        ).flow.map { pagingData ->
            pagingData.map { it.toDomain() }
        }.flowOn(ioDispatcher)
    }

    override suspend fun connect(roomId: Long, onConnected: () -> Unit, onError: (Throwable) -> Unit) {
        tokenDataSource.getAccessToken()?.let {
            webSocketManager.connect(it, roomId, onConnected, onError)
        }
    }

    override suspend fun subscribeRoom(
        roomId: Long,
        onUnreadUpdate: (UnreadBroadcast) -> Unit
    ) {
        webSocketManager.subscribeRoom(
            roomId = roomId,
            onMessage =  { dto ->
                scope.launch {
                    withContext(ioDispatcher){
                        saveMessageToDatabase(dto, roomId)
                    }
                }
            },
            onUnreadUpdate = { dto ->
                scope.launch {
                    withContext(ioDispatcher){
                        updateUnreadCount(
                            messageId = dto.messageId,
                            roomId = roomId,
                            unreadCount = dto.unreadCount
                        )
                    }
                }
            }
        )
    }

    override fun sendMessage(roomId: Long, content: String) {
        webSocketManager.sendMessage(roomId, content)
    }

    override fun sendReadEvent(roomId: Long) {
        webSocketManager.sendReadEvent(roomId)
    }

    override fun leaveRoom(roomId: Long) {
        webSocketManager.leaveRoom(roomId)
    }

    private suspend fun saveMessageToDatabase(message: Message, roomId: Long) {
        dao.insert(message.toEntity(roomId))
    }

    private suspend fun updateUnreadCount(messageId: Long, roomId: Long, unreadCount: Int) {
        dao.updateUnreadCount(messageId, roomId, unreadCount)
    }
}