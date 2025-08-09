package com.stone.fridge.core.paging

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.stone.fridge.core.database.GoDatabase
import com.stone.fridge.core.database.model.PostItemEntity
import com.stone.fridge.core.database.model.toEntity
import com.stone.fridge.core.network.service.PostClient
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

@OptIn(ExperimentalPagingApi::class)
class PostPagingSource @Inject constructor(
    private val postClient: PostClient,
    private val db: GoDatabase,
    private val keyword: String? = null,
): RemoteMediator<Int, PostItemEntity>() {

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, PostItemEntity>
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

            val response = postClient.searchPosts(keyword = keyword, page = loadKey)
            db.withTransaction {
                if(loadType == LoadType.REFRESH) {
                    db.postItemDao().clearAll()
                }
                val postEntities = response.result!!.content.map { it.toEntity(response.result!!.number + 1) }
                db.postItemDao().upsertAll(postEntities)
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


