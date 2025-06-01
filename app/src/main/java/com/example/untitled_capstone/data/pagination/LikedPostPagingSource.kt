package com.example.untitled_capstone.data.pagination

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.example.untitled_capstone.data.local.db.LikedPostDatabase
import com.example.untitled_capstone.data.local.db.MyPostDatabase
import com.example.untitled_capstone.data.local.db.PostItemDatabase
import com.example.untitled_capstone.data.local.entity.LikedPostEntity
import com.example.untitled_capstone.data.local.entity.PostItemEntity
import com.example.untitled_capstone.data.remote.service.PostApi
import retrofit2.HttpException
import java.io.IOException

@OptIn(ExperimentalPagingApi::class)
class LikedPostPagingSource(
    private val api: PostApi,
    private val db: LikedPostDatabase
): RemoteMediator<Int, LikedPostEntity>() {

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, LikedPostEntity>
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

            val response = api.getLikedPosts(page = loadKey.toInt(), size = state.config.pageSize)

            db.withTransaction {
                if(loadType == LoadType.REFRESH) {
                    db.dao.clearAll()
                }
                val postEntities = response.result!!.content.map { it.toLikedPostEntity(response.result.number + 1) }
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


