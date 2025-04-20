package com.example.untitled_capstone.domain.repository

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
    suspend fun post(newPost: NewPost, images: List<File>?): Resource<String>
    fun getMyPosts(fetchType: PostFetchType): Flow<PagingData<PostItemEntity>>
    suspend fun getPostById(id: Long): Resource<Post>
    suspend fun deletePost(id: Long): Resource<String>
    suspend fun modifyPost(id: Long, newPost: NewPost): Resource<String>
    fun searchPosts(fetchType: PostFetchType): Flow<PagingData<PostItemEntity>>
    suspend fun toggleLike(id: Long): Resource<Boolean>
    fun getLikedPosts(fetchType: PostFetchType): Flow<PagingData<PostItemEntity>>
    suspend fun getNickname(): String?
    suspend fun uploadImages(id: Long, images: List<MultipartBody.Part>): Resource<String>
    suspend fun deleteImage(id: Long, imageId: Long): Resource<String>
    suspend fun getSearchHistory(): Resource<List<Keyword>>
    suspend fun deleteSearchHistory(keyword: String): Resource<String>
    suspend fun deleteAllSearchHistory(): Resource<String>
    suspend fun reportPost(postId: Long, reportType: String, content: String): Resource<String>
}