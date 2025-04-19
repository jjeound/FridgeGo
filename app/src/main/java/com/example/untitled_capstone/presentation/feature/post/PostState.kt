package com.example.untitled_capstone.presentation.feature.post

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.example.untitled_capstone.domain.model.Post

class PostState{
    var post by mutableStateOf<Post?>(null)
    var isLoading by mutableStateOf(false)
}