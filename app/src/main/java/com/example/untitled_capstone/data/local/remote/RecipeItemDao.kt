package com.example.untitled_capstone.data.local.remote

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.example.untitled_capstone.data.local.entity.RecipeItemEntity

@Dao
interface RecipeItemDao {
    @Upsert
    suspend fun upsertAll(items: List<RecipeItemEntity>)

    @Query("DELETE FROM fridgeitementity")
    suspend fun clearAll()

    @Query("SELECT * FROM fridgeitementity")
    fun getRecipeItems(): PagingSource<Int, RecipeItemEntity>
}






