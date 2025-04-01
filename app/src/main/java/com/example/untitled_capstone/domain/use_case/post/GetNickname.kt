package com.example.untitled_capstone.domain.use_case.post

import com.example.untitled_capstone.domain.repository.PostRepository
import javax.inject.Inject

class GetNickname @Inject constructor(
    private val repository: PostRepository
) {
    operator fun invoke(): String{
        return repository.getNickname()
    }
}