package com.example.untitled_capstone.domain.use_case.post

import com.example.untitled_capstone.core.util.Resource
import com.example.untitled_capstone.domain.model.Post
import com.example.untitled_capstone.domain.repository.PostRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetPostByIdUseCase @Inject constructor(
    private val repository: PostRepository
) {
    suspend operator fun invoke(id: Long): Flow<Resource<Post>> {
        return repository.getPostById(id)
    }
}