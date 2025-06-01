package com.example.untitled_capstone.domain.use_case.home

import com.example.untitled_capstone.core.util.Resource
import com.example.untitled_capstone.domain.repository.HomeRepository
import kotlinx.coroutines.flow.Flow
import java.io.File
import javax.inject.Inject

class UploadRecipeImageUseCase @Inject constructor(
    private val repository: HomeRepository,
) {
    operator fun invoke(id: Long, file: File): Flow<Resource<String>> {
        return repository.uploadImage(id, file)
    }
}