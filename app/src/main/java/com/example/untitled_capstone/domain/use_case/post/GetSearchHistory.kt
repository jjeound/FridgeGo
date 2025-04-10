package com.example.untitled_capstone.domain.use_case.post

import com.example.untitled_capstone.core.util.Resource
import com.example.untitled_capstone.domain.model.Keyword
import com.example.untitled_capstone.domain.repository.PostRepository
import javax.inject.Inject

class GetSearchHistory @Inject constructor(
    private val repository: PostRepository
) {
    suspend operator fun invoke(): Resource<List<Keyword>> {
        return repository.getSearchHistory()
    }
}