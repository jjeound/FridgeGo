package com.example.untitled_capstone.domain.repository

import androidx.paging.PagingData
import com.example.untitled_capstone.core.util.Resource
import com.example.untitled_capstone.data.local.entity.PostItemEntity
import com.example.untitled_capstone.data.remote.dto.ApiResponse
import com.example.untitled_capstone.data.remote.dto.NewPostDto
import kotlinx.coroutines.flow.Flow

interface PostRepository {
    suspend fun post(newPostDto: NewPostDto): Resource<ApiResponse>
    fun getPosts(): Flow<PagingData<PostItemEntity>>
}