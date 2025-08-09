package com.stone.fridge.core.data.post

import androidx.annotation.WorkerThread
import androidx.paging.PagingData
import com.stone.fridge.core.model.Keyword
import com.stone.fridge.core.model.ModifyPost
import com.stone.fridge.core.model.NewPost
import com.stone.fridge.core.model.Post
import com.stone.fridge.core.model.PostRaw
import kotlinx.coroutines.flow.Flow
import java.io.File

interface PostRepository {
    @WorkerThread
    fun post(newPost: NewPost, images: List<File>?): Flow<String>
    @WorkerThread
    fun getMyPosts(): Flow<PagingData<PostRaw>>
    @WorkerThread
    fun getPostById(id: Long): Flow<Post>
    @WorkerThread
    fun deletePost(id: Long): Flow<String>
    @WorkerThread
    fun modifyPost(id: Long, modifyPost: ModifyPost): Flow<String>
    @WorkerThread
    fun searchPosts(keyword: String?): Flow<PagingData<PostRaw>>
    @WorkerThread
    fun toggleLike(id: Long): Flow<Boolean>
    @WorkerThread
    fun getLikedPosts(): Flow<PagingData<PostRaw>>
    @WorkerThread
    fun uploadImages(id: Long, images: List<File>): Flow<String>
    @WorkerThread
    fun deleteImage(id: Long, imageId: Long): Flow<String>
    @WorkerThread
    fun getSearchHistory(): Flow<List<Keyword>>
    @WorkerThread
    fun deleteSearchHistory(keyword: String): Flow<String>
    @WorkerThread
    fun deleteAllSearchHistory(): Flow<String>
    @WorkerThread
    fun reportPost(postId: Long, reportType: String, content: String): Flow<String>
}