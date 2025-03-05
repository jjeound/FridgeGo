package com.example.untitled_capstone.presentation.feature.shopping.event

import com.example.untitled_capstone.domain.model.Post


sealed interface PostAction{
    data object LoadItems: PostAction
    data class AddNewPost(val post: Post): PostAction
    data class ToggleLike(val id: Int): PostAction
}