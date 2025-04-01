package com.example.untitled_capstone.domain.use_case.post

import com.example.untitled_capstone.core.util.Resource
import com.example.untitled_capstone.domain.model.Post
import com.example.untitled_capstone.domain.repository.PostRepository
import javax.inject.Inject

class GetPostById @Inject constructor(
    private val repository: PostRepository
) {
    suspend operator fun invoke(id: Long): Resource<Post> {
        return repository.getPostById(id)
    }
}