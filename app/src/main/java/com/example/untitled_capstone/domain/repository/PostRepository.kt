package com.example.untitled_capstone.domain.repository

import androidx.annotation.WorkerThread
import androidx.paging.PagingData
import com.example.untitled_capstone.core.util.Resource
import com.example.untitled_capstone.data.local.entity.PostItemEntity
import com.example.untitled_capstone.data.util.PostFetchType
import com.example.untitled_capstone.domain.model.Keyword
import com.example.untitled_capstone.domain.model.NewPost
import com.example.untitled_capstone.domain.model.Post
import kotlinx.coroutines.flow.Flow
import okhttp3.MultipartBody
import java.io.File

interface PostRepository {
    @WorkerThread
    suspend fun post(newPost: NewPost, images: List<File>?): Flow<Resource<String>>
    @WorkerThread
    fun getMyPosts(fetchType: PostFetchType): Flow<PagingData<PostItemEntity>>
    @WorkerThread
    suspend fun getPostById(id: Long): Flow<Resource<Post>>
    @WorkerThread
    suspend fun deletePost(id: Long): Flow<Resource<String>>
    @WorkerThread
    suspend fun modifyPost(id: Long, newPost: NewPost): Flow<Resource<String>>
    @WorkerThread
    fun searchPosts(fetchType: PostFetchType): Flow<PagingData<PostItemEntity>>
    @WorkerThread
    suspend fun toggleLike(id: Long): Flow<Resource<Boolean>>
    @WorkerThread
    fun getLikedPosts(fetchType: PostFetchType): Flow<PagingData<PostItemEntity>>
    @WorkerThread
    suspend fun uploadImages(id: Long, images: List<MultipartBody.Part>): Flow<Resource<String>>
    @WorkerThread
    suspend fun deleteImage(id: Long, imageId: Long): Flow<Resource<String>>
    @WorkerThread
    suspend fun getSearchHistory(): Flow<Resource<List<Keyword>>>
    @WorkerThread
    suspend fun deleteSearchHistory(keyword: String): Flow<Resource<String>>
    @WorkerThread
    suspend fun deleteAllSearchHistory(): Flow<Resource<String>>
    @WorkerThread
    suspend fun reportPost(postId: Long, reportType: String, content: String): Flow<Resource<String>>
}