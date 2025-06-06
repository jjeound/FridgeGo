package com.stone.fridge.data.local.remote

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.stone.fridge.data.local.entity.MyPostEntity

@Dao
interface MyPostDao {
    @Upsert
    suspend fun upsertAll(posts: List<MyPostEntity>)

    @Query("DELETE FROM mypostentity")
    suspend fun clearAll()

    @Query("SELECT * FROM mypostentity")
    fun getPostItems(): PagingSource<Int, MyPostEntity>
}






