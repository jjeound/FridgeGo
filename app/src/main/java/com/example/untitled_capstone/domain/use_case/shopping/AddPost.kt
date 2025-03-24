package com.example.untitled_capstone.domain.use_case.shopping

import com.example.untitled_capstone.core.util.Resource
import com.example.untitled_capstone.data.remote.dto.ApiResponse
import com.example.untitled_capstone.domain.model.NewPost
import com.example.untitled_capstone.domain.repository.ShoppingRepository
import javax.inject.Inject


class AddPost @Inject constructor(
    private val repository: ShoppingRepository
) {
    suspend operator fun invoke(post: NewPost): Resource<ApiResponse> {
        return repository.post(post.toPostDto())
    }
}