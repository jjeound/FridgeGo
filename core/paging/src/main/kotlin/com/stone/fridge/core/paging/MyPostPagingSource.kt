package com.stone.fridge.core.paging

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.stone.fridge.core.database.GoDatabase
import com.stone.fridge.core.database.model.MyPostEntity
import com.stone.fridge.core.database.model.toMyEntity
import com.stone.fridge.core.network.service.PostClient
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

@OptIn(ExperimentalPagingApi::class)
class MyPostPagingSource @Inject constructor(
    private val postClient: PostClient,
    private val db: GoDatabase
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

            val response = postClient.getPosts(page = loadKey)

            db.withTransaction {
                if(loadType == LoadType.REFRESH) {
                    db.myPostDao().clearAll()
                }
                val postEntities = response.result!!.content.map { it.toMyEntity(response.result!!.number + 1) }
                db.myPostDao().upsertAll(postEntities)
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


