package com.stone.fridge.domain.repository

import androidx.annotation.WorkerThread
import androidx.paging.PagingData
import com.stone.fridge.core.util.Resource
import com.stone.fridge.data.local.entity.LikedPostEntity
import com.stone.fridge.data.local.entity.MyPostEntity
import com.stone.fridge.data.local.entity.PostItemEntity
import com.stone.fridge.domain.model.Keyword
import com.stone.fridge.domain.model.NewPost
import com.stone.fridge.domain.model.Post
import kotlinx.coroutines.flow.Flow
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File

interface PostRepository {
    @WorkerThread
    fun post(newPost: RequestBody, images: List<MultipartBody.Part>?): Flow<Resource<String>>
    @WorkerThread
    fun getMyPosts(): Flow<PagingData<MyPostEntity>>
    @WorkerThread
    fun getPostById(id: Long): Flow<Resource<Post>>
    @WorkerThread
    fun deletePost(id: Long): Flow<Resource<String>>
    @WorkerThread
    fun modifyPost(id: Long, newPost: NewPost): Flow<Resource<String>>
    @WorkerThread
    fun searchPosts(keyword: String?): Flow<PagingData<PostItemEntity>>
    @WorkerThread
    fun toggleLike(id: Long): Flow<Resource<Boolean>>
    @WorkerThread
    fun getLikedPosts(): Flow<PagingData<LikedPostEntity>>
    @WorkerThread
    fun uploadImages(id: Long, images: List<File>): Flow<Resource<String>>
    @WorkerThread
    fun deleteImage(id: Long, imageId: Long): Flow<Resource<String>>
    @WorkerThread
    fun getSearchHistory(): Flow<Resource<List<Keyword>>>
    @WorkerThread
    fun deleteSearchHistory(keyword: String): Flow<Resource<String>>
    @WorkerThread
    fun deleteAllSearchHistory(): Flow<Resource<String>>
    @WorkerThread
    fun reportPost(postId: Long, reportType: String, content: String): Flow<Resource<String>>
}