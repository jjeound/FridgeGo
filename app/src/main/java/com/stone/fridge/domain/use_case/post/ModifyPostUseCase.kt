package com.stone.fridge.domain.use_case.post

import com.stone.fridge.core.util.Resource
import com.stone.fridge.domain.model.NewPost
import com.stone.fridge.domain.repository.PostRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ModifyPostUseCase @Inject constructor(
    private val repository: PostRepository
) {
    operator fun invoke(id: Long, newPost: NewPost): Flow<Resource<String>> {
        return repository.modifyPost(id, newPost)
    }
}