package com.stone.fridge.domain.use_case.home

import com.stone.fridge.core.util.Resource
import com.stone.fridge.domain.repository.HomeRepository
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