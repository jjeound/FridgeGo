package com.stone.fridge.core.data.post

import android.content.Context
import androidx.annotation.WorkerThread
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingData
import androidx.paging.map
import com.google.gson.Gson
import com.stone.fridge.core.data.util.ImageCompressor
import com.stone.fridge.core.database.model.LikedPostEntity
import com.stone.fridge.core.database.model.MyPostEntity
import com.stone.fridge.core.database.model.PostItemEntity
import com.stone.fridge.core.database.model.toDomain
import com.stone.fridge.core.model.Keyword
import com.stone.fridge.core.model.ModifyPost
import com.stone.fridge.core.model.NewPost
import com.stone.fridge.core.model.Post
import com.stone.fridge.core.model.PostRaw
import com.stone.fridge.core.model.Report
import com.stone.fridge.core.network.AppDispatchers
import com.stone.fridge.core.network.Dispatcher
import com.stone.fridge.core.network.service.PostClient
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import javax.inject.Inject

class PostRepositoryImpl @Inject constructor(
    private val postClient: PostClient,
    private val pager: Pager<Int, PostItemEntity>,
    private val myPager: Pager<Int, MyPostEntity>,
    private val likedPager: Pager<Int, LikedPostEntity>,
    private val gson: Gson,
    @param:ApplicationContext private val context: Context,
    @param:Dispatcher(AppDispatchers.IO) private val ioDispatcher: CoroutineDispatcher,
): PostRepository{
    @WorkerThread
    override fun post(newPost: NewPost, images: List<File>?): Flow<String> = flow {
        val json = gson.toJson(newPost)
        val jsonBody = json.toRequestBody("application/json; charset=utf-8".toMediaType())
        val compressedImages = images?.map {
            ImageCompressor.compressImage(context, it)
        }
        val requestFiles = compressedImages?.map { it.asRequestBody("image/*".toMediaTypeOrNull()) }
        val body = requestFiles?.mapIndexed { index, file ->
            MultipartBody.Part.createFormData("postImages", compressedImages[index].name, file)
        }
        postClient.writePost(jsonBody, body).result?.let {
            emit(it)
        }
    }.flowOn(ioDispatcher)

    @OptIn(ExperimentalPagingApi::class)
    @WorkerThread
    override fun getMyPosts(): Flow<PagingData<PostRaw>> {
        return myPager.flow.map { pagingData ->
            pagingData.map { it.toDomain() }
        }.flowOn(ioDispatcher)
    }

    @WorkerThread
    override fun getPostById(id: Long): Flow<Post> = flow {
        postClient.getPostById(id).result?.let {
            emit(it)
        }
    }.flowOn(ioDispatcher)

    @WorkerThread
    override fun deletePost(id: Long): Flow<String> = flow {
        postClient.deletePost(id).result?.let {
            emit(it)
        }
    }.flowOn(ioDispatcher)

    @WorkerThread
    override fun modifyPost(
        id: Long,
        modifyPost: ModifyPost,
    ): Flow<String> = flow {
        postClient.modifyPost(id, modifyPost).result?.let {
            emit(it)
        }
    }.flowOn(ioDispatcher)

    @OptIn(ExperimentalPagingApi::class)
    @WorkerThread
    override fun searchPosts(keyword: String?): Flow<PagingData<PostRaw>> {
        return pager.flow.map { pagingData ->
            pagingData.map { it.toDomain() }
        }.flowOn(ioDispatcher)
    }

    @WorkerThread
    override fun toggleLike(id: Long): Flow<Boolean> = flow {
        postClient.toggleLike(id).result?.let {
            emit(it.liked)
        }
    }.flowOn(ioDispatcher)

    @OptIn(ExperimentalPagingApi::class)
    @WorkerThread
    override fun getLikedPosts(): Flow<PagingData<PostRaw>> {
        return likedPager.flow.map { pagingData ->
            pagingData.map { it.toDomain() }
        }.flowOn(ioDispatcher)
    }

    @WorkerThread
    override fun uploadImages(
        id: Long,
        images: List<File>
    ): Flow<String> = flow {
        val compressedImages = images.map {
            ImageCompressor.compressImage(context, it)
        }
        val requestFile = compressedImages.map { it.asRequestBody("image/*".toMediaTypeOrNull()) }
        val body = requestFile.mapIndexed { index, file ->
            MultipartBody.Part.createFormData("postImage", compressedImages[index].name, file)
        }
        postClient.uploadPostImages(id, body).result?.let {
            emit(it)
        }
    }.flowOn(ioDispatcher)

    @WorkerThread
    override fun deleteImage(
        id: Long,
        imageId: Long
    ): Flow<String> = flow {
        postClient.deletePostImage(id, imageId).result?.let {
            emit(it)
        }
    }.flowOn(ioDispatcher)

    @WorkerThread
    override fun getSearchHistory(): Flow<List<Keyword>> = flow {
        postClient.getSearchHistory().result?.let {
            emit(it)
        }
    }.flowOn(ioDispatcher)

    @WorkerThread
    override fun deleteSearchHistory(keyword: String): Flow<String> = flow {
        postClient.deleteSearchHistory(keyword).result?.let {
            emit(it)
        }
    }.flowOn(ioDispatcher)

    @WorkerThread
    override fun deleteAllSearchHistory(): Flow<String> = flow {
        postClient.deleteAllSearchHistory().result?.let {
            emit(it)
        }
    }.flowOn(ioDispatcher)

    @WorkerThread
    override fun reportPost(
        postId: Long,
        reportType: String,
        content: String
    ): Flow<String> = flow {
        postClient.reportPost(postId, Report(reportType, content)).result?.let {
            emit(it)
        }
    }.flowOn(ioDispatcher)
}