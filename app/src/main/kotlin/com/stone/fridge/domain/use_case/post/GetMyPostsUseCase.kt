package com.stone.fridge.domain.use_case.post

import androidx.paging.PagingData
import androidx.paging.map
import com.stone.fridge.domain.model.PostRaw
import com.stone.fridge.domain.repository.PostRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetMyPostsUseCase @Inject constructor(
    private val repository: PostRepository
) {
    operator fun invoke(): Flow<PagingData<PostRaw>> {
        return repository.getMyPosts().map { pagingData ->
            pagingData.map { it.toPostRaw() }
        }
    }
}