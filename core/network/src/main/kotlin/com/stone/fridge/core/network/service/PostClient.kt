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
import javax.inject.Inject

class PostClient @Inject constructor(
    private val postApi: PostApi,
) {
    suspend fun writePost(
        post: RequestBody,
        postImages: List<MultipartBody.Part>? = null
    ): ApiResponse<String> =
        postApi.writePost(post, postImages)

    suspend fun getPosts(
        page: Int = 1
    ): ApiResponse<PostResult> =
        postApi.getPosts(page, PAGE_SIZE)

    suspend fun getPostById(
        postId: Long
    ): ApiResponse<Post> =
        postApi.getPostById(postId)

    suspend fun deletePost(
        postId: Long
    ): ApiResponse<String> =
        postApi.deletePost(postId)

    suspend fun modifyPost(
        postId: Long,
        newPostDto: ModifyPost
    ): ApiResponse<String> =
        postApi.modifyPost(postId, newPostDto)

    suspend fun searchPosts(
        keyword: String? = null,
        page: Int = 1
    ): ApiResponse<PostResult> =
        postApi.searchPosts(keyword, page, PAGE_SIZE)

    suspend fun toggleLike(
        postId: Long
    ): ApiResponse<PostLiked> =
        postApi.toggleLike(postId)

    suspend fun getLikedPosts(
        page: Int = 1
    ): ApiResponse<PostResult> =
        postApi.getLikedPosts(page, PAGE_SIZE)

    suspend fun uploadPostImages(
        postId: Long,
        postImages: List<MultipartBody.Part>
    ): ApiResponse<String> =
        postApi.uploadPostImages(postId, postImages
    )

    suspend fun deletePostImage(
        postId: Long,
        imageId: Long
    ): ApiResponse<String> =
        postApi.deletePostImage(postId, imageId)

    suspend fun getSearchHistory(): ApiResponse<List<Keyword>> =
        postApi.getSearchHistory()

    suspend fun deleteSearchHistory(
        keyword: String
    ): ApiResponse<String> =
        postApi.deleteSearchHistory(keyword)

    suspend fun deleteAllSearchHistory(): ApiResponse<String> =
        postApi.deleteAllSearchHistory()

    suspend fun reportPost(
        postId: Long,
        report: Report
    ): ApiResponse<String> =
        postApi.reportPost(postId, report)

    companion object{
        const val PAGE_SIZE = 10
    }
}