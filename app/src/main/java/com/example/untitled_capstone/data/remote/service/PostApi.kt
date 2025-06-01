package com.example.untitled_capstone.data.remote.service

import com.example.untitled_capstone.data.remote.dto.ApiResponse
import com.example.untitled_capstone.data.remote.dto.ModifyPostDto
import com.example.untitled_capstone.data.remote.dto.PostDto
import com.example.untitled_capstone.data.remote.dto.PostLikedDto
import com.example.untitled_capstone.data.remote.dto.PostResultDto
import com.example.untitled_capstone.data.remote.dto.ReportDto
import com.example.untitled_capstone.data.remote.dto.SearchedKeywordDto
import okhttp3.MultipartBody
import okhttp3.RequestBody
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

    @Multipart
    @POST("/api/post")
    suspend fun post(
        @Part("post") post: RequestBody,
        @Part postImages: List<MultipartBody.Part>? = null
    ): ApiResponse<String>

    @GET("/api/post")
    suspend fun getPosts(
        @Query("page") page: Int = 1,
        @Query("size") size: Int = 10,
    ): ApiResponse<PostResultDto>

    @GET("/api/post/{postId}")
    suspend fun getPostById(
        @Path("postId") postId: Long
    ): ApiResponse<PostDto>

    @DELETE("/api/post/{postId}")
    suspend fun deletePost(
        @Path("postId") postId: Long
    ): ApiResponse<String>

    @PATCH("/api/post/{postId}")
    suspend fun modifyPost(
        @Path("postId") postId: Long,
        @Body newPostDto: ModifyPostDto
    ): ApiResponse<String>

    @GET("/api/post/search")
    suspend fun searchPosts(
        @Query("keyword") keyword: String? = null,
        @Query("page") page: Int = 1,
        @Query("size") size: Int = 10,
    ): ApiResponse<PostResultDto>

    @PATCH("/api/like/post/{postId}")
    suspend fun toggleLike(
        @Path("postId") postId: Long,
    ): ApiResponse<PostLikedDto>

    @GET("/api/like/post")
    suspend fun getLikedPosts(
        @Query("page") page: Int = 1,
        @Query("size") size: Int = 10,
    ): ApiResponse<PostResultDto>

    @Multipart
    @POST("/api/s3/add-post-image/{postId}")
    suspend fun uploadPostImages(
        @Path("postId") postId: Long,
        @Part postImage: List<MultipartBody.Part>
    ): ApiResponse<String>

    @DELETE("/api/s3/delete-post-image/{postId}/{imageId}")
    suspend fun deletePostImage(
        @Path("postId") postId: Long,
        @Path("imageId") imageId: Long
    ): ApiResponse<String>

    @GET("/api/search-history")
    suspend fun getSearchHistory(): ApiResponse<List<SearchedKeywordDto>>

    @DELETE("/api/search-history")
    suspend fun deleteSearchHistory(
        @Query("keyword") keyword: String
    ): ApiResponse<String>

    @DELETE("/api/search-history/all")
    suspend fun deleteAllSearchHistory(): ApiResponse<String>

    @POST("/api/report/post/{postId}")
    suspend fun reportPost(
        @Path ("postId") postId: Long,
        @Body report: ReportDto
    ): ApiResponse<String>
}