package com.stone.fridge.core.database.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.stone.fridge.core.database.model.PostItemEntity

@Dao
interface PostItemDao {
    @Upsert
    suspend fun upsertAll(items: List<PostItemEntity>)

    @Query("DELETE FROM postitementity")
    suspend fun clearAll()

    @Query("SELECT * FROM postitementity")
    fun getPostItems(): PagingSource<Int, PostItemEntity>
}






