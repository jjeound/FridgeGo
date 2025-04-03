package com.example.untitled_capstone.data.pagination

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.example.untitled_capstone.data.local.db.FridgeItemDatabase
import com.example.untitled_capstone.data.local.entity.FridgeItemEntity
import com.example.untitled_capstone.data.remote.service.FridgeApi
import com.example.untitled_capstone.data.util.FridgeFetchType
import retrofit2.HttpException
import java.io.IOException

@OptIn(ExperimentalPagingApi::class)
class FridgePagingSource(
    private val api: FridgeApi,
    private val db: FridgeItemDatabase,
    private val fetchType: FridgeFetchType,
): RemoteMediator<Int, FridgeItemEntity>() {

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, FridgeItemEntity>
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

            val response = when (fetchType) {
                is FridgeFetchType.OrderByCreated -> api.getFridgeItems(
                    page = loadKey.toInt(),
                    size = state.config.pageSize
                )
                is FridgeFetchType.OrderByDate -> api.getFridgeItemsByDate(
                    page = loadKey.toInt(),
                    size = state.config.pageSize
                )
            }

            db.withTransaction {
                if(loadType == LoadType.REFRESH) {
                    db.dao.clearAll()
                }
                val fridgeEntities = response.result!!.content.map { it.toFridgeItemEntity(response.result.number + 1) }
                db.dao.upsertAll(fridgeEntities)
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


