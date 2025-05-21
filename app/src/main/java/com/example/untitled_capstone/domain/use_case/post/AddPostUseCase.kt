package com.example.untitled_capstone.domain.use_case.post

import com.example.untitled_capstone.core.util.Resource
import com.example.untitled_capstone.domain.model.NewPost
import com.example.untitled_capstone.domain.repository.PostRepository
import kotlinx.coroutines.flow.Flow
import java.io.File
import javax.inject.Inject


class AddPostUseCase @Inject constructor(
    private val repository: PostRepository
) {
    suspend operator fun invoke(post: NewPost, images: List<File>?): Flow<Resource<String>> {
        return repository.post(post, images)
    }
}