package com.example.untitled_capstone.data.pagination

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.example.untitled_capstone.data.local.db.PostItemDatabase
import com.example.untitled_capstone.data.local.entity.PostItemEntity
import com.example.untitled_capstone.data.remote.service.PostApi
import com.example.untitled_capstone.data.util.PostFetchType
import retrofit2.HttpException
import java.io.IOException

@OptIn(ExperimentalPagingApi::class)
class PostPagingSource(
    private val api: PostApi,
    private val db: PostItemDatabase,
    private val fetchType: PostFetchType,
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

            val response = when (fetchType) {
                is PostFetchType.MyPosts -> api.getPosts(page = loadKey.toInt(), size = state.config.pageSize)
                is PostFetchType.LikedPosts -> api.getLikedPosts(page = loadKey.toInt(), size = state.config.pageSize)
                is PostFetchType.Search -> api.searchPosts(keyword = fetchType.keyword, page = loadKey.toInt(), size = state.config.pageSize)
            }


            db.withTransaction {
                if(loadType == LoadType.REFRESH) {
                    db.dao.clearAll()
                }
                val postEntities = response.result!!.content.map { it.toPostEntity(response.result.number + 1) }
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


