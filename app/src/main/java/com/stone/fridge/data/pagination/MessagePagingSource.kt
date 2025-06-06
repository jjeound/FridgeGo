package com.stone.fridge.data.pagination

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.stone.fridge.data.local.db.MessageItemDatabase
import com.stone.fridge.data.local.entity.MessageItemEntity
import com.stone.fridge.data.remote.service.ChatApi

@OptIn(ExperimentalPagingApi::class)
class MessagePagingSource(
    private val roomId: Long,
    private val api: ChatApi,
    private val db: MessageItemDatabase
) : RemoteMediator<Int, MessageItemEntity>() {

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, MessageItemEntity>
    ): MediatorResult {
        return try {
            val messageDao = db.dao

            val lastMessageId = when (loadType) {
                LoadType.REFRESH -> null
                LoadType.PREPEND -> return MediatorResult.Success(endOfPaginationReached = true)
                LoadType.APPEND -> {
                    val lastItem = state.lastItemOrNull()
                    lastItem?.messageId
                }
            }

            val response = api.getMessages(
                roomId = roomId,
                lastMessageId = lastMessageId,
                size = state.config.pageSize
            )

            val remoteMessages = response.result ?: emptyList()

            db.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    messageDao.clearMessages(roomId)
                }

                messageDao.insertAll(remoteMessages.map {
                    it.toMessageEntity(roomId)
                })
            }

            MediatorResult.Success(endOfPaginationReached = remoteMessages.isEmpty())
        } catch (e: Exception) {
            MediatorResult.Error(e)
        }
    }
}