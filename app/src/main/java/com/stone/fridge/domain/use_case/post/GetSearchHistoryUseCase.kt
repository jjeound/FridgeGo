package com.stone.fridge.domain.use_case.post

import com.stone.fridge.core.util.Resource
import com.stone.fridge.domain.model.Keyword
import com.stone.fridge.domain.repository.PostRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetSearchHistoryUseCase @Inject constructor(
    private val repository: PostRepository
) {
    suspend operator fun invoke(): Flow<Resource<List<Keyword>>> {
        return repository.getSearchHistory()
    }
}