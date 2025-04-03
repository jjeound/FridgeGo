package com.example.untitled_capstone.data.pagination

import android.util.Log
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.example.untitled_capstone.data.local.db.RecipeItemDatabase
import com.example.untitled_capstone.data.local.entity.RecipeItemEntity
import com.example.untitled_capstone.data.remote.service.HomeApi
import retrofit2.HttpException
import java.io.IOException

@OptIn(ExperimentalPagingApi::class)
class RecipePagingSource(
    private val api: HomeApi,
    private val db: RecipeItemDatabase,
): RemoteMediator<Int, RecipeItemEntity>() {

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, RecipeItemEntity>
    ): MediatorResult {
        return try {
            val loadKey = when(loadType) {
                LoadType.REFRESH -> 1
                LoadType.PREPEND -> return MediatorResult.Success(
                    endOfPaginationReached = true
                )
                LoadType.APPEND -> {
                    val lastItem = state.lastItemOrNull()
                    if(lastItem == null) {
                        return MediatorResult.Success(endOfPaginationReached = true)
                    } else {
                        lastItem.pagerNumber + 1
                    }
                }
            }

            val response = api.getRecipe(
                page = loadKey.toInt(),
                size = state.config.pageSize
            )

            db.withTransaction {
                if(loadType == LoadType.REFRESH) {
                    db.dao.clearAll()
                }
                val recipeEntities = response.result!!.content.map { it.toRecipeEntity(response.result.number + 1) }
                db.dao.upsertAll(recipeEntities)
            }

            MediatorResult.Success(
                endOfPaginationReached = response.result!!.last
            )
        } catch(e: IOException) {
            MediatorResult.Error(e)
        } catch(e: HttpException) {
            MediatorResult.Error(e)
        }
    }
}


