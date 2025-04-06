package com.example.untitled_capstone.data.remote.service

import com.example.untitled_capstone.data.remote.dto.AddPostResponse
import com.example.untitled_capstone.data.remote.dto.ApiResponse
import com.example.untitled_capstone.data.remote.dto.GetPostByIdResponse
import com.example.untitled_capstone.data.remote.dto.NewPostDto
import com.example.untitled_capstone.data.remote.dto.PostLikedResponse
import com.example.untitled_capstone.data.remote.dto.PostResponse
import okhttp3.MultipartBody
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query

interface PostApi {

    @POST("/api/post")
    suspend fun post(
        @Body newPostDto: NewPostDto
    ): AddPostResponse

    @GET("/api/post")
    suspend fun getPosts(
        @Query("page") page: Int = 1,
        @Query("size") size: Int = 10,
    ): PostResponse

    @GET("/api/post/{postId}")
    suspend fun getPostById(
        @Path("postId") postId: Long
    ): GetPostByIdResponse

    @DELETE("/api/post/{postId}")
    suspend fun deletePost(
        @Path("postId") postId: Long
    ): ApiResponse

    @PATCH("/api/post/{postId}")
    suspend fun modifyPost(
        @Path("postId") postId: Long,
        @Body newPostDto: NewPostDto
    ): ApiResponse

    @GET("/api/post/search")
    suspend fun searchPosts(
        @Query("keyword") keyword: String? = null,
        @Query("page") page: Int = 1,
        @Query("size") size: Int = 10,
    ): PostResponse

    @PATCH("/api/like/post/{postId}")
    suspend fun toggleLike(
        @Path("postId") postId: Long,
    ): PostLikedResponse

    @GET("/api/like/post")
    suspend fun getLikedPosts(
        @Query("page") page: Int = 1,
        @Query("size") size: Int = 10,
    ): PostResponse

    @Multipart
    @POST("/api/s3/update-post/{postId}")
    suspend fun uploadPostImages(
        @Path("postId") postId: Long,
        @Part postImage: List<MultipartBody.Part>
    ): ApiResponse
}