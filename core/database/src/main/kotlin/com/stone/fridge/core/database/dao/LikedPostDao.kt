package com.stone.fridge.core.database.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.stone.fridge.core.database.model.LikedPostEntity

@Dao
interface LikedPostDao {
    @Upsert
    suspend fun upsertAll(posts: List<LikedPostEntity>)

    @Query("DELETE FROM likedpostentity")
    suspend fun clearAll()

    @Query("SELECT * FROM likedpostentity")
    fun getPostItems(): PagingSource<Int, LikedPostEntity>
}






