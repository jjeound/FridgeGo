package com.example.untitled_capstone.data.repository

import android.content.Context
import android.util.Log
import androidx.annotation.WorkerThread
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.untitled_capstone.core.util.Constants.NETWORK_PAGE_SIZE
import com.example.untitled_capstone.core.util.Resource
import com.example.untitled_capstone.data.AppDispatchers
import com.example.untitled_capstone.data.Dispatcher
import com.example.untitled_capstone.data.local.db.LikedPostDatabase
import com.example.untitled_capstone.data.local.db.MyPostDatabase
import com.example.untitled_capstone.data.local.db.PostItemDatabase
import com.example.untitled_capstone.data.local.entity.LikedPostEntity
import com.example.untitled_capstone.data.local.entity.MyPostEntity
import com.example.untitled_capstone.data.local.entity.PostItemEntity
import com.example.untitled_capstone.data.pagination.LikedPostPagingSource
import com.example.untitled_capstone.data.pagination.MyPostPagingSource
import com.example.untitled_capstone.data.pagination.PostPagingSource
import com.example.untitled_capstone.data.remote.dto.ReportDto
import com.example.untitled_capstone.data.remote.service.PostApi
import com.example.untitled_capstone.domain.model.Keyword
import com.example.untitled_capstone.domain.model.NewPost
import com.example.untitled_capstone.domain.model.Post
import com.example.untitled_capstone.domain.repository.PostRepository
import com.example.untitled_capstone.data.util.ImageCompressor
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okio.IOException
import org.json.JSONObject
import retrofit2.HttpException
import java.io.File
import javax.inject.Inject

class PostRepositoryImpl @Inject constructor(
    private val api: PostApi,
    private val postDb: PostItemDatabase,
    private val myPostDb: MyPostDatabase,
    private val likedPostDb: LikedPostDatabase,
    @ApplicationContext private val context: Context,
    @Dispatcher(AppDispatchers.IO) private val ioDispatcher: CoroutineDispatcher,
): PostRepository{
    @WorkerThread
    override fun post(newPost: RequestBody, images: List<MultipartBody.Part>?): Flow<Resource<String>> = flow {
        emit(Resource.Loading())
        try {
            val response = api.post(newPost, images)
            if(response.isSuccess){
                emit(Resource.Success(response.result))
            }else {
                emit(Resource.Error(message = response.message))
            }
        } catch (e: IOException) {
            emit(Resource.Error(e.toString()))
        } catch (e: HttpException) {
            val errorMessage = try {
                val errorJson = e.response()?.errorBody()?.string()
                val errorObj = JSONObject(errorJson ?: "")
                errorObj.getString("message")
            } catch (_: Exception) {
                "알 수 없는 오류가 발생했어요."
            }
            emit(Resource.Error(errorMessage))
        }
    }.flowOn(ioDispatcher)

    @OptIn(ExperimentalPagingApi::class)
    @WorkerThread
    override fun getMyPosts(): Flow<PagingData<MyPostEntity>> {
        return Pager(
            config = PagingConfig(pageSize = NETWORK_PAGE_SIZE, enablePlaceholders = false),
            remoteMediator = MyPostPagingSource(api, myPostDb),
            pagingSourceFactory = { myPostDb.dao.getPostItems() }
        ).flow.flowOn(ioDispatcher)
    }

    @WorkerThread
    override fun getPostById(id: Long): Flow<Resource<Post>> = flow {
        emit(Resource.Loading())
        try {
            val response = api.getPostById(id)
            if(response.isSuccess){
                emit(Resource.Success(response.result!!.toPost()))
            }else {
                emit(Resource.Error(message = response.message))
            }
        } catch (e: IOException) {
            emit(Resource.Error(e.toString()))
        } catch (e: HttpException) {
            val errorMessage = try {
                val errorJson = e.response()?.errorBody()?.string()
                val errorObj = JSONObject(errorJson ?: "")
                errorObj.getString("message")
            } catch (_: Exception) {
                "알 수 없는 오류가 발생했어요."
            }
            emit(Resource.Error(errorMessage))
        }
    }.flowOn(ioDispatcher)

    @WorkerThread
    override fun deletePost(id: Long): Flow<Resource<String>> = flow {
        emit(Resource.Loading())
        try {
            val response = api.deletePost(id)
            if(response.isSuccess){
                emit(Resource.Success(response.result))
            }else {
                emit(Resource.Error(message = response.message))
            }
        } catch (e: IOException) {
            emit(Resource.Error(e.toString()))
        } catch (e: HttpException) {
            val errorMessage = try {
                val errorJson = e.response()?.errorBody()?.string()
                val errorObj = JSONObject(errorJson ?: "")
                errorObj.getString("message")
            } catch (_: Exception) {
                "알 수 없는 오류가 발생했어요."
            }
            emit(Resource.Error(errorMessage))
        }
    }.flowOn(ioDispatcher)

    @WorkerThread
    override fun modifyPost(
        id: Long,
        newPost: NewPost,
    ): Flow<Resource<String>> = flow {
        emit(Resource.Loading())
        try {
            val response = api.modifyPost(id, newPost.toModifyPostDto())
            if(response.isSuccess){
                emit(Resource.Success(response.result))
            }else {
                emit(Resource.Error(message = response.message))
            }
        } catch (e: IOException) {
            emit(Resource.Error(e.toString()))
        } catch (e: HttpException) {
            val errorMessage = try {
                val errorJson = e.response()?.errorBody()?.string()
                val errorObj = JSONObject(errorJson ?: "")
                errorObj.getString("message")
            } catch (_: Exception) {
                "알 수 없는 오류가 발생했어요."
            }
            emit(Resource.Error(errorMessage))
        }
    }.flowOn(ioDispatcher)

    @OptIn(ExperimentalPagingApi::class)
    @WorkerThread
    override fun searchPosts(keyword: String?): Flow<PagingData<PostItemEntity>> {
        return Pager(
            config = PagingConfig(pageSize = NETWORK_PAGE_SIZE, enablePlaceholders = false),
            remoteMediator = PostPagingSource(api, postDb, keyword),
            pagingSourceFactory = { postDb.dao.getPostItems() }
        ).flow.flowOn(ioDispatcher)
    }

    @WorkerThread
    override fun toggleLike(id: Long): Flow<Resource<Boolean>> = flow {
        emit(Resource.Loading())
        try {
            val response = api.toggleLike(id)
            if(response.isSuccess){
                emit(Resource.Success(response.result!!.liked))
            }else {
                emit(Resource.Error(message = response.message))
            }
        } catch (e: IOException) {
            emit(Resource.Error(e.toString()))
        } catch (e: HttpException) {
            val errorMessage = try {
                val errorJson = e.response()?.errorBody()?.string()
                val errorObj = JSONObject(errorJson ?: "")
                errorObj.getString("message")
            } catch (_: Exception) {
                "알 수 없는 오류가 발생했어요."
            }
            emit(Resource.Error(errorMessage))
        }
    }.flowOn(ioDispatcher)

    @OptIn(ExperimentalPagingApi::class)
    @WorkerThread
    override fun getLikedPosts(): Flow<PagingData<LikedPostEntity>> {
        return Pager(
            config = PagingConfig(pageSize = NETWORK_PAGE_SIZE, enablePlaceholders = false),
            remoteMediator = LikedPostPagingSource(api, likedPostDb),
            pagingSourceFactory = { likedPostDb.dao.getPostItems() }
        ).flow.flowOn(ioDispatcher)
    }

    @WorkerThread
    override fun uploadImages(
        id: Long,
        images: List<File>
    ): Flow<Resource<String>> = flow {
        emit(Resource.Loading())
        val compressedImages = images.map {
            ImageCompressor.compressImage(context, it)
        }
        val requestFile = compressedImages.map { it.asRequestBody("image/*".toMediaTypeOrNull())}
        val body = requestFile.mapIndexed { index, file ->
            MultipartBody.Part.createFormData("postImage", compressedImages[index].name, file)
        }
        try {
            val response = api.uploadPostImages(id, body)
            if(response.isSuccess){
                emit(Resource.Success(response.result))
            }else {
                emit(Resource.Error(message = response.message))
            }
        } catch (e: IOException) {
            emit(Resource.Error(e.toString()))
        } catch (e: HttpException) {
            val errorMessage = try {
                val errorJson = e.response()?.errorBody()?.string()
                val errorObj = JSONObject(errorJson ?: "")
                errorObj.getString("message")
            } catch (_: Exception) {
                "알 수 없는 오류가 발생했어요."
            }
            emit(Resource.Error(errorMessage))
        }
    }.flowOn(ioDispatcher)

    @WorkerThread
    override fun deleteImage(
        id: Long,
        imageId: Long
    ): Flow<Resource<String>> = flow {
        emit(Resource.Loading())
        try {
            val response = api.deletePostImage(id, imageId)
            if(response.isSuccess){
                emit(Resource.Success(response.result))
            }else {
                emit(Resource.Error(message = response.message))
            }
        } catch (e: IOException) {
            emit(Resource.Error(e.toString()))
        } catch (e: HttpException) {
            val errorMessage = try {
                val errorJson = e.response()?.errorBody()?.string()
                val errorObj = JSONObject(errorJson ?: "")
                errorObj.getString("message")
            } catch (_: Exception) {
                "알 수 없는 오류가 발생했어요."
            }
            emit(Resource.Error(errorMessage))
        }
    }.flowOn(ioDispatcher)

    @WorkerThread
    override fun getSearchHistory(): Flow<Resource<List<Keyword>>> = flow {
        emit(Resource.Loading())
        try {
            val response = api.getSearchHistory()
            if(response.isSuccess){
                Log.d("response", response.toString())
                emit(Resource.Success(response.result?.map { it.toKeyword() }))
            }else {
                emit(Resource.Error(message = response.toString()))
            }
        } catch (e: IOException) {
            emit(Resource.Error(e.toString()))
        } catch (e: HttpException) {
            val errorMessage = try {
                val errorJson = e.response()?.errorBody()?.string()
                val errorObj = JSONObject(errorJson ?: "")
                errorObj.getString("message")
            } catch (_: Exception) {
                "알 수 없는 오류가 발생했어요."
            }
            emit(Resource.Error(errorMessage))
        }
    }.flowOn(ioDispatcher)

    @WorkerThread
    override fun deleteSearchHistory(keyword: String): Flow<Resource<String>> = flow {
        emit(Resource.Loading())
        try {
            val response = api.deleteSearchHistory(keyword)
            if(response.isSuccess){
                emit(Resource.Success(response.result))
            }else {
                emit(Resource.Error(message = response.message))
            }
        } catch (e: IOException) {
            emit(Resource.Error(e.toString()))
        } catch (e: HttpException) {
            val errorMessage = try {
                val errorJson = e.response()?.errorBody()?.string()
                val errorObj = JSONObject(errorJson ?: "")
                errorObj.getString("message")
            } catch (_: Exception) {
                "알 수 없는 오류가 발생했어요."
            }
            emit(Resource.Error(errorMessage))
        }
    }

    @WorkerThread
    override fun deleteAllSearchHistory(): Flow<Resource<String>> = flow {
        emit(Resource.Loading())
        try {
            val response = api.deleteAllSearchHistory()
            if(response.isSuccess){
                emit(Resource.Success(response.result))
            }else {
                emit(Resource.Error(message = response.message))
            }
        } catch (e: IOException) {
            emit(Resource.Error(e.toString()))
        } catch (e: HttpException) {
            val errorMessage = try {
                val errorJson = e.response()?.errorBody()?.string()
                val errorObj = JSONObject(errorJson ?: "")
                errorObj.getString("message")
            } catch (_: Exception) {
                "알 수 없는 오류가 발생했어요."
            }
            emit(Resource.Error(errorMessage))
        }
    }.flowOn(ioDispatcher)

    @WorkerThread
    override fun reportPost(
        postId: Long,
        reportType: String,
        content: String
    ): Flow<Resource<String>> = flow {
        emit(Resource.Loading())
        try {
            val response = api.reportPost(postId, ReportDto(reportType, content))
            if(response.isSuccess){
                emit(Resource.Success(response.result))
            }else {
                emit(Resource.Error(message = response.message))
            }
        } catch (e: IOException) {
            emit(Resource.Error(e.toString()))
        } catch (e: HttpException) {
            val errorMessage = try {
                val errorJson = e.response()?.errorBody()?.string()
                val errorObj = JSONObject(errorJson ?: "")
                errorObj.getString("message")
            } catch (_: Exception) {
                "알 수 없는 오류가 발생했어요."
            }
            emit(Resource.Error(errorMessage))
        }
    }.flowOn(ioDispatcher)
}