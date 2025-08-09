package com.stone.fridge.core.paging

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.stone.fridge.core.database.model.FridgeItemEntity
import com.stone.fridge.core.database.GoDatabase
import com.stone.fridge.core.database.model.toEntity
import com.stone.fridge.core.network.service.FridgeClient
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import retrofit2.HttpException
import java.io.IOException

@OptIn(ExperimentalPagingApi::class)
class FridgePagingSource @AssistedInject constructor(
    private val fridgeClient: FridgeClient,
    private val db: GoDatabase,
    @Assisted private val fetchType: FridgeFetchType,
): RemoteMediator<Int, FridgeItemEntity>() {

    @AssistedFactory
    interface Factory {
        fun create(fetchType: FridgeFetchType): FridgePagingSource
    }

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
                is FridgeFetchType.OrderByCreated -> fridgeClient.getFridgeItems(
                    page = loadKey,
                )
                is FridgeFetchType.OrderByDate -> fridgeClient.getFridgeItemsByDate(
                    page = loadKey,
                )
            }

            db.withTransaction {
                if(loadType == LoadType.REFRESH) {
                    db.fridgeItemDao().clearAll()
                }
                val fridgeEntities = response.result!!.content.map { it.toEntity(response.result!!.number + 1) }
                db.fridgeItemDao().upsertAll(fridgeEntities)
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


