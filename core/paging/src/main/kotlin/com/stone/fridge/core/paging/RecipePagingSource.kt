package com.stone.fridge.core.paging

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.stone.fridge.core.database.GoDatabase
import com.stone.fridge.core.database.model.RecipeItemEntity
import com.stone.fridge.core.database.model.toEntity
import com.stone.fridge.core.network.service.HomeClient
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

@OptIn(ExperimentalPagingApi::class)
class RecipePagingSource @Inject constructor(
    private val homeClient: HomeClient,
    private val db: GoDatabase,
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

            val response = homeClient.getRecipe(
                page = loadKey,
            )

            db.withTransaction {
                if(loadType == LoadType.REFRESH) {
                    db.recipeItemDao().clearAll()
                }
                val recipeEntities = response.result!!.content.map { it.toEntity(response.result!!.number + 1) }
                db.recipeItemDao().upsertAll(recipeEntities)
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


