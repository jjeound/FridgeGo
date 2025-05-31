package com.example.untitled_capstone.domain.use_case.post

import com.example.untitled_capstone.core.util.Resource
import com.example.untitled_capstone.domain.repository.PostRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class DeletePostImageUseCase @Inject constructor(
    private val repository: PostRepository
) {
    operator fun invoke(id: Long, imageId: Long): Flow<Resource<String>> {
        return repository.deleteImage(id, imageId)
    }
}