package com.example.untitled_capstone.data.local.remote

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.untitled_capstone.data.local.entity.FridgeItemEntity

@Dao
interface FridgeItemDao {

    @Insert(onConflict = OnConflictStrategy.Companion.REPLACE)
    suspend fun insertFridgeItem(item: FridgeItemEntity)

    @Query("DELETE FROM fridgeitementity WHERE id = :id")
    suspend fun deleteFridgeItem(id: Int)

    @Update
    suspend fun upsertAll(fridgeItems: List<FridgeItemEntity>)

    @Query("DELETE FROM fridgeitementity")
    suspend fun clearAll()

    @Query("SELECT * FROM fridgeitementity")
    fun getFridgeItems(): PagingSource<Int, FridgeItemEntity>
}