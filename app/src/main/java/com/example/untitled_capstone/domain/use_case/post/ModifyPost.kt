package com.example.untitled_capstone.domain.use_case.post

import com.example.untitled_capstone.core.util.Resource
import com.example.untitled_capstone.data.remote.dto.ApiResponse
import com.example.untitled_capstone.domain.model.NewPost
import com.example.untitled_capstone.domain.repository.PostRepository
import javax.inject.Inject

class ModifyPost @Inject constructor(
    private val repository: PostRepository
) {
    suspend operator fun invoke(id: Long, newPost: NewPost): Resource<ApiResponse> {
        return repository.modifyPost(id, newPost.toNewPostDto())
    }
}