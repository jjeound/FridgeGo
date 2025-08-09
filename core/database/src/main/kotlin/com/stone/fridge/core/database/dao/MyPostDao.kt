package com.stone.fridge.core.database.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.stone.fridge.core.database.model.MyPostEntity

@Dao
interface MyPostDao {
    @Upsert
    suspend fun upsertAll(posts: List<MyPostEntity>)

    @Query("DELETE FROM mypostentity")
    suspend fun clearAll()

    @Query("SELECT * FROM mypostentity")
    fun getPostItems(): PagingSource<Int, MyPostEntity>
}






