package com.example.untitled_capstone.domain.use_case.post

import com.example.untitled_capstone.core.util.Resource
import com.example.untitled_capstone.data.remote.dto.ApiResponse
import com.example.untitled_capstone.data.remote.dto.PostLikedDto
import com.example.untitled_capstone.domain.repository.PostRepository
import javax.inject.Inject

class ToggleLikePost @Inject constructor(
    private val repository: PostRepository
) {
    suspend operator fun invoke(id: Long): Resource<PostLikedDto> {
        return repository.toggleLike(id)
    }
}