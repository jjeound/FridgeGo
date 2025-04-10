package com.example.untitled_capstone.presentation.feature.post

import com.example.untitled_capstone.domain.model.Keyword
import com.example.untitled_capstone.domain.model.NewPost
import java.io.File

sealed interface PostEvent{
    data object LoadItems: PostEvent
    data class AddNewPost(val post: NewPost, val images: List<File>? = null): PostEvent
    data class ToggleLike(val id: Long): PostEvent
    data class DeletePost(val id: Long): PostEvent
    data class ModifyPost(val id: Long, val newPost: NewPost): PostEvent
    data class SearchPost(val keyword: String): PostEvent
    data class GetPostById(val id: Long): PostEvent
    data object GetMyPosts: PostEvent
    data object GetLikedPosts: PostEvent
    data object InitState: PostEvent
    data class UploadPostImages(val id: Long, val images: List<File>): PostEvent
    data class DeletePostImage(val id: Long, val imageId: Long): PostEvent
    data object GetSearchHistory: PostEvent
    data class DeleteSearchHistory(val keyword: String): PostEvent
    data object DeleteAllSearchHistory: PostEvent
    data class AddSearchHistory(val word: Keyword): PostEvent
}