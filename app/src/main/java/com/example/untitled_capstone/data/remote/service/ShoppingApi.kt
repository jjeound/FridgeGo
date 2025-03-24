package com.example.untitled_capstone.data.remote.service

import com.example.untitled_capstone.data.remote.dto.ApiResponse
import com.example.untitled_capstone.data.remote.dto.PostDto
import retrofit2.http.Body
import retrofit2.http.POST

interface ShoppingApi {

    @POST("/api/post")
    suspend fun post(
        @Body postDto: PostDto
    ): ApiResponse


}