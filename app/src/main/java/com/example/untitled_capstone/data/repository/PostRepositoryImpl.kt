package com.example.untitled_capstone.data.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.untitled_capstone.core.util.Resource
import com.example.untitled_capstone.data.local.db.PostItemDatabase
import com.example.untitled_capstone.data.local.entity.PostItemEntity
import com.example.untitled_capstone.data.pagination.PostPagingSource
import com.example.untitled_capstone.data.remote.dto.ApiResponse
import com.example.untitled_capstone.data.remote.dto.NewPostDto
import com.example.untitled_capstone.data.remote.service.PostApi
import com.example.untitled_capstone.data.repository.FridgeRepositoryImpl.Companion.NETWORK_PAGE_SIZE
import com.example.untitled_capstone.domain.repository.PostRepository
import kotlinx.coroutines.flow.Flow
import okio.IOException
import retrofit2.HttpException
import javax.inject.Inject

class PostRepositoryImpl @Inject constructor(
    private val api: PostApi,
    private val db: PostItemDatabase,
): PostRepository {
    override suspend fun post(newPostDto: NewPostDto): Resource<ApiResponse> {
        return try {
            Resource.Loading(data = null)
            val response = api.post(newPostDto)
            if(response.isSuccess){
                Resource.Success(response)
            }else {
                Resource.Error(message = response.toString())
            }
        } catch (e: IOException) {
            Resource.Error(e.toString())
        } catch (e: HttpException) {
            Resource.Error(e.toString())
        }

    }

    @OptIn(ExperimentalPagingApi::class)
    override fun getPosts(): Flow<PagingData<PostItemEntity>> {
        return Pager(
            config = PagingConfig(pageSize = NETWORK_PAGE_SIZE, enablePlaceholders = false),
            remoteMediator = PostPagingSource(api, db),
            pagingSourceFactory = { db.dao.getPostItems() }
        ).flow
    }
}