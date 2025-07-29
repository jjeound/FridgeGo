package com.stone.fridge.data.pagination

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.stone.fridge.data.local.db.MyPostDatabase
import com.stone.fridge.data.local.entity.MyPostEntity
import com.stone.fridge.data.remote.service.PostApi
import retrofit2.HttpException
import java.io.IOException

@OptIn(ExperimentalPagingApi::class)
class MyPostPagingSource(
    private val api: PostApi,
    private val db: MyPostDatabase
): RemoteMediator<Int, MyPostEntity>() {

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, MyPostEntity>
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

            val response = api.getPosts(page = loadKey.toInt(), size = state.config.pageSize)

            db.withTransaction {
                if(loadType == LoadType.REFRESH) {
                    db.dao.clearAll()
                }
                val postEntities = response.result!!.content.map { it.toMyPostEntity(response.result.number + 1) }
                db.dao.upsertAll(postEntities)
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


