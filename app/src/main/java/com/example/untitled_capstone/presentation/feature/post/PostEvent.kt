package com.example.untitled_capstone.presentation.feature.post

import com.example.untitled_capstone.domain.model.NewPost

sealed interface PostEvent{
    data object LoadItems: PostEvent
    data class AddNewPost(val post: NewPost): PostEvent
    data class ToggleLike(val id: Long): PostEvent
}