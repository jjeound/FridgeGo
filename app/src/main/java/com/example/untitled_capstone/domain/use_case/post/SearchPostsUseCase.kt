package com.example.untitled_capstone.domain.use_case.post

import androidx.paging.PagingData
import androidx.paging.map
import com.example.untitled_capstone.domain.model.PostRaw
import com.example.untitled_capstone.domain.repository.PostRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class SearchPostsUseCase @Inject constructor(
    private val repository: PostRepository
) {
    operator fun invoke(keyword: String? = null): Flow<PagingData<PostRaw>> {
        return repository.searchPosts(keyword).map { pagingData ->
            pagingData.map { it.toPostRaw() }
        }
    }
}