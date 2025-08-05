package com.stone.fridge.core.paging

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.stone.fridge.core.database.GoDatabase
import com.stone.fridge.core.database.model.MessageItemEntity
import com.stone.fridge.core.database.model.toEntity
import com.stone.fridge.core.network.service.ChatClient

@OptIn(ExperimentalPagingApi::class)
class MessagePagingSource(
    private val roomId: Long,
    private val chatClient: ChatClient,
    private val db: GoDatabase
) : RemoteMediator<Int, MessageItemEntity>() {

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, MessageItemEntity>
    ): MediatorResult {
        return try {
            val messageDao = db.messageItemDao()

            val lastMessageId = when (loadType) {
                LoadType.REFRESH -> null
                LoadType.PREPEND -> return MediatorResult.Success(endOfPaginationReached = true)
                LoadType.APPEND -> {
                    val lastItem = state.lastItemOrNull()
                    lastItem?.messageId
                }
            }

            val response = chatClient.getMessages(
                roomId = roomId,
                lastMessageId = lastMessageId,
            )

            val remoteMessages = response.result ?: emptyList()

            db.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    messageDao.clearMessages(roomId)
                }

                messageDao.insertAll(remoteMessages.map {
                    it.toEntity(roomId)
                })
            }

            MediatorResult.Success(endOfPaginationReached = remoteMessages.isEmpty())
        } catch (e: Exception) {
            MediatorResult.Error(e)
        }
    }
}