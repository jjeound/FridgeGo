package com.stone.fridge.core.network.service

import com.stone.fridge.core.model.ModifyPost
import com.stone.fridge.core.model.Post
import com.stone.fridge.core.model.PostLiked
import com.stone.fridge.core.model.PostResult
import com.stone.fridge.core.model.Report
import com.stone.fridge.core.model.Keyword
import com.stone.fridge.core.network.model.ApiResponse
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
    suspend fun writePost(
        @Part("post") post: RequestBody,
        @Part postImages: List<MultipartBody.Part>? = null
    ): ApiResponse<String>

    @GET("/api/post")
    suspend fun getPosts(
        @Query("page") page: Int = 1,
        @Query("size") size: Int = 10,
    ): ApiResponse<PostResult>

    @GET("/api/post/{postId}")
    suspend fun getPostById(
        @Path("postId") postId: Long
    ): ApiResponse<Post>

    @DELETE("/api/post/{postId}")
    suspend fun deletePost(
        @Path("postId") postId: Long
    ): ApiResponse<String>

    @PATCH("/api/post/{postId}")
    suspend fun modifyPost(
        @Path("postId") postId: Long,
        @Body newPostDto: ModifyPost
    ): ApiResponse<String>

    @GET("/api/post/search")
    suspend fun searchPosts(
        @Query("keyword") keyword: String? = null,
        @Query("page") page: Int = 1,
        @Query("size") size: Int = 10,
    ): ApiResponse<PostResult>

    @PATCH("/api/like/post/{postId}")
    suspend fun toggleLike(
        @Path("postId") postId: Long,
    ): ApiResponse<PostLiked>

    @GET("/api/like/post")
    suspend fun getLikedPosts(
        @Query("page") page: Int = 1,
        @Query("size") size: Int = 10,
    ): ApiResponse<PostResult>

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
    suspend fun getSearchHistory(): ApiResponse<List<Keyword>>

    @DELETE("/api/search-history")
    suspend fun deleteSearchHistory(
        @Query("keyword") keyword: String
    ): ApiResponse<String>

    @DELETE("/api/search-history/all")
    suspend fun deleteAllSearchHistory(): ApiResponse<String>

    @POST("/api/report/post/{postId}")
    suspend fun reportPost(
        @Path ("postId") postId: Long,
        @Body report: Report
    ): ApiResponse<String>
}