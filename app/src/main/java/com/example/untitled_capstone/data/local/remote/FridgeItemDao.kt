package com.example.untitled_capstone.data.local.remote

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.example.untitled_capstone.data.local.entity.FridgeItemEntity

@Dao
interface FridgeItemDao {
    @Upsert
    suspend fun upsertAll(items: List<FridgeItemEntity>)

    @Query("DELETE FROM fridgeitementity")
    suspend fun clearAll()

    @Query("SELECT * FROM fridgeitementity")
    fun getFridgeItems(): PagingSource<Int, FridgeItemEntity>
}