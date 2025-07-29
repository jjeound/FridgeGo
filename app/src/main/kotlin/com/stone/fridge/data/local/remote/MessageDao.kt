package com.stone.fridge.data.local.remote

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.stone.fridge.data.local.entity.MessageItemEntity

@Dao
interface MessageDao {

    @Query("SELECT * FROM messageitementity WHERE roomId = :roomId ORDER BY messageId DESC")
    fun getMessagesPaging(roomId: Long): PagingSource<Int, MessageItemEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(messages: List<MessageItemEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(message: MessageItemEntity)

    @Query("DELETE FROM messageitementity WHERE roomId = :roomId")
    suspend fun clearMessages(roomId: Long)

    @Query("UPDATE messageitementity SET unreadCount = :unreadCount WHERE roomId = :roomId and messageId = :messageId")
    suspend fun updateUnreadCount(messageId: Long, roomId: Long, unreadCount: Int)
}