package com.stone.fridge.core.database.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.stone.fridge.core.database.model.RecipeItemEntity

@Dao
interface RecipeItemDao {
    @Upsert
    suspend fun upsertAll(items: List<RecipeItemEntity>)

    @Query("DELETE FROM recipeitementity")
    suspend fun clearAll()

    @Query("SELECT * FROM recipeitementity")
    fun getRecipeItems(): PagingSource<Int, RecipeItemEntity>
}






