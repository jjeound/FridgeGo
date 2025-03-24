package com.example.untitled_capstone.domain.repository

import com.example.untitled_capstone.core.util.Resource
import com.example.untitled_capstone.data.remote.dto.ApiResponse
import com.example.untitled_capstone.data.remote.dto.PostDto

interface ShoppingRepository {
    suspend fun post(postDto: PostDto): Resource<ApiResponse>
}