package com.example.untitled_capstone.data.repository

import android.content.Context
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.untitled_capstone.core.util.Constants.NETWORK_PAGE_SIZE
import com.example.untitled_capstone.core.util.PrefKeys.NICKNAME
import com.example.untitled_capstone.core.util.Resource
import com.example.untitled_capstone.data.local.db.PostItemDatabase
import com.example.untitled_capstone.data.local.entity.PostItemEntity
import com.example.untitled_capstone.data.pagination.PostPagingSource
import com.example.untitled_capstone.data.remote.dto.ReportDto
import com.example.untitled_capstone.data.remote.service.PostApi
import com.example.untitled_capstone.data.util.PostFetchType
import com.example.untitled_capstone.domain.model.Keyword
import com.example.untitled_capstone.domain.model.NewPost
import com.example.untitled_capstone.domain.model.Post
import com.example.untitled_capstone.domain.repository.PostRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okio.IOException
import retrofit2.HttpException
import javax.inject.Inject

class PostRepositoryImpl @Inject constructor(
    private val api: PostApi,
    private val db: PostItemDatabase,
    @ApplicationContext context: Context
): PostRepository {
    val dataStore = context.dataStore

    override suspend fun post(newPost: RequestBody, images: List<MultipartBody.Part>?): Resource<String> {
        return try {
            Resource.Loading(data = null)
            val response = api.post(newPost, images)
            if(response.isSuccess){
                Resource.Success(response.result)
            }else {
                Resource.Error(message = response.message)
            }
        } catch (e: IOException) {
            Resource.Error(e.toString())
        } catch (e: HttpException) {
            Resource.Error(e.toString())
        }

    }

    @OptIn(ExperimentalPagingApi::class)
    override fun getMyPosts(fetchType: PostFetchType): Flow<PagingData<PostItemEntity>> {
        return Pager(
            config = PagingConfig(pageSize = NETWORK_PAGE_SIZE, enablePlaceholders = false),
            remoteMediator = PostPagingSource(api, db, fetchType),
            pagingSourceFactory = { db.dao.getPostItems() }
        ).flow
    }

    override suspend fun getPostById(id: Long): Resource<Post> {
        return try {
            Resource.Loading(data = null)
            val response = api.getPostById(id)
            if(response.isSuccess){
                Resource.Success(response.result!!.toPost())
            }else {
                Resource.Error(message = response.message)
            }
        } catch (e: IOException) {
            Resource.Error(e.toString())
        } catch (e: HttpException) {
            Resource.Error(e.toString())
        }
    }

    override suspend fun deletePost(id: Long): Resource<String> {
        return try {
            Resource.Loading(data = null)
            val response = api.deletePost(id)
            if(response.isSuccess){
                db.dao.clearAll() //페이징 꼬여서 다 지워야 함
                Resource.Success(response.result)
            }else {
                Resource.Error(message = response.message)
            }
        } catch (e: IOException) {
            Resource.Error(e.toString())
        } catch (e: HttpException) {
            Resource.Error(e.toString())
        }
    }

    override suspend fun modifyPost(
        id: Long,
        newPost: NewPost
    ): Resource<String> {
        return try {
            Resource.Loading(data = null)
            val response = api.modifyPost(id, newPost.toNewPostDto())
            if(response.isSuccess){
                Resource.Success(response.result)
            }else {
                Resource.Error(message = response.message)
            }
        } catch (e: IOException) {
            Resource.Error(e.toString())
        } catch (e: HttpException) {
            Resource.Error(e.toString())
        }
    }

    @OptIn(ExperimentalPagingApi::class)
    override fun searchPosts(fetchType: PostFetchType): Flow<PagingData<PostItemEntity>> {
        return Pager(
            config = PagingConfig(pageSize = NETWORK_PAGE_SIZE, enablePlaceholders = false),
            remoteMediator = PostPagingSource(api, db, fetchType),
            pagingSourceFactory = { db.dao.getPostItems() }
        ).flow
    }

    override suspend fun toggleLike(id: Long): Resource<Boolean> {
        return try {
            Resource.Loading(data = null)
            val response = api.toggleLike(id)
            if(response.isSuccess){
                Resource.Success(response.result!!.liked)
            }else {
                Resource.Error(message = response.message)
            }
        } catch (e: IOException) {
            Resource.Error(e.toString())
        } catch (e: HttpException) {
            Resource.Error(e.toString())
        }
    }

    @OptIn(ExperimentalPagingApi::class)
    override fun getLikedPosts(fetchType: PostFetchType): Flow<PagingData<PostItemEntity>> {
        return Pager(
            config = PagingConfig(pageSize = NETWORK_PAGE_SIZE, enablePlaceholders = false),
            remoteMediator = PostPagingSource(api, db, fetchType),
            pagingSourceFactory = { db.dao.getPostItems() }
        ).flow
    }

    override suspend fun getNickname(): String? {
        return dataStore.data.map { prefs ->
            prefs[NICKNAME]
        }.first()
    }

    override suspend fun uploadImages(
        id: Long,
        images: List<MultipartBody.Part>
    ): Resource<String> {
        return try {
            Resource.Loading(data = null)
            val response = api.uploadPostImages(id, images)
            if(response.isSuccess){
                Resource.Success(response.result)
            }else {
                Resource.Error(message = response.message)
            }
        } catch (e: IOException) {
            Resource.Error(e.toString())
        } catch (e: HttpException) {
            Resource.Error(e.toString())
        }
    }

    override suspend fun deleteImage(
        id: Long,
        imageId: Long
    ): Resource<String> {
        return try {
            Resource.Loading(data = null)
            val response = api.deletePostImage(id, imageId)
            if(response.isSuccess){
                Resource.Success(response.result)
            }else {
                Resource.Error(message = response.message)
            }
        } catch (e: IOException) {
            Resource.Error(e.toString())
        } catch (e: HttpException) {
            Resource.Error(e.toString())
        }
    }

    override suspend fun getSearchHistory(): Resource<List<Keyword>> {
        return try {
            Resource.Loading(data = null)
            val response = api.getSearchHistory()
            if(response.isSuccess){
                Resource.Success(response.result?.map { it.toKeyword() })
            }else {
                Resource.Error(message = response.toString())
            }
        } catch (e: IOException) {
            Resource.Error(e.toString())
        } catch (e: HttpException) {
            Resource.Error(e.toString())
        }
    }

    override suspend fun deleteSearchHistory(keyword: String): Resource<String> {
        return try {
            Resource.Loading(data = null)
            val response = api.deleteSearchHistory(keyword)
            if(response.isSuccess){
                Resource.Success(response.result)
            }else {
                Resource.Error(message = response.message)
            }
        } catch (e: IOException) {
            Resource.Error(e.toString())
        } catch (e: HttpException) {
            Resource.Error(e.toString())
        }
    }

    override suspend fun deleteAllSearchHistory(): Resource<String> {
        return try {
            Resource.Loading(data = null)
            val response = api.deleteAllSearchHistory()
            if(response.isSuccess){
                Resource.Success(response.result)
            }else {
                Resource.Error(message = response.message)
            }
        } catch (e: IOException) {
            Resource.Error(e.toString())
        } catch (e: HttpException) {
            Resource.Error(e.toString())
        }
    }

    override suspend fun reportPost(
        postId: Long,
        reportType: String,
        content: String
    ): Resource<String> {
        return try {
            Resource.Loading(data = null)
            val response = api.reportPost(postId, ReportDto(reportType, content))
            if(response.isSuccess){
                Resource.Success(response.result)
            }else {
                Resource.Error(message = response.message)
            }
        } catch (e: IOException) {
            Resource.Error(e.toString())
        } catch (e: HttpException) {
            Resource.Error(e.toString())
        }
    }
}