package com.stone.fridge.domain.use_case.post

import com.stone.fridge.core.util.Resource
import com.stone.fridge.domain.repository.PostRepository
import kotlinx.coroutines.flow.Flow
import java.io.File
import javax.inject.Inject

class UploadPostImagesUseCase @Inject constructor(
    private val repository: PostRepository,
) {
    operator fun invoke(id: Long, images: List<File>): Flow<Resource<String>> {
        return repository.uploadImages(id, images)
    }
}