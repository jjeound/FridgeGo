package com.example.untitled_capstone.domain.repository

import androidx.paging.PagingData
import com.example.untitled_capstone.core.util.Resource
import com.example.untitled_capstone.data.local.entity.PostItemEntity
import com.example.untitled_capstone.data.remote.dto.ApiResponse
import com.example.untitled_capstone.data.remote.dto.NewPostDto
import com.example.untitled_capstone.data.util.PostFetchType
import com.example.untitled_capstone.domain.model.Post
import kotlinx.coroutines.flow.Flow

interface PostRepository {
    suspend fun post(newPostDto: NewPostDto): Resource<ApiResponse>
    fun getMyPosts(fetchType: PostFetchType): Flow<PagingData<PostItemEntity>>
    suspend fun getPostById(id: Long): Resource<Post>
    suspend fun deletePost(id: Long): Resource<ApiResponse>
    suspend fun modifyPost(id: Long, newPostDto: NewPostDto): Resource<ApiResponse>
    fun searchPosts(fetchType: PostFetchType): Flow<PagingData<PostItemEntity>>
    suspend fun toggleLike(id: Long): Resource<ApiResponse>
    fun getLikedPosts(fetchType: PostFetchType): Flow<PagingData<PostItemEntity>>
    fun getNickname(): String
}