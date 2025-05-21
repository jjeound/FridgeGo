package com.example.untitled_capstone.domain.use_case.post

import com.example.untitled_capstone.core.util.Resource
import com.example.untitled_capstone.domain.repository.PostRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ToggleLikePostUseCase @Inject constructor(
    private val repository: PostRepository
) {
    suspend operator fun invoke(id: Long): Flow<Resource<Boolean>> {
        return repository.toggleLike(id)
    }
}