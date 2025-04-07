package com.example.untitled_capstone.domain.use_case.post

import com.example.untitled_capstone.core.util.Resource
import com.example.untitled_capstone.data.remote.dto.ApiResponse
import com.example.untitled_capstone.domain.repository.PostRepository
import javax.inject.Inject

class DeletePostImage @Inject constructor(
    private val repository: PostRepository
) {
    suspend operator fun invoke(id: Long, imageId: Long): Resource<ApiResponse> {
        return repository.deleteImage(id, imageId)
    }
}