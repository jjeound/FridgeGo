package com.example.untitled_capstone.data.remote.service

import com.example.untitled_capstone.data.remote.dto.ApiResponse
import com.example.untitled_capstone.data.remote.dto.NewPostDto
import com.example.untitled_capstone.data.remote.dto.PostResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface PostApi {

    @POST("/api/post")
    suspend fun post(
        @Body newPostDto: NewPostDto
    ): ApiResponse

    @GET("/api/post")
    suspend fun getPosts(
        @Query("page") page: Int = 1,
        @Query("size") size: Int = 10,
    ): PostResponse
}