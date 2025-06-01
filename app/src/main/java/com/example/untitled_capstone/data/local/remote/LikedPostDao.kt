package com.example.untitled_capstone.data.local.remote

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.example.untitled_capstone.data.local.entity.LikedPostEntity

@Dao
interface LikedPostDao {
    @Upsert
    suspend fun upsertAll(posts: List<LikedPostEntity>)

    @Query("DELETE FROM likedpostentity")
    suspend fun clearAll()

    @Query("SELECT * FROM likedpostentity")
    fun getPostItems(): PagingSource<Int, LikedPostEntity>
}






