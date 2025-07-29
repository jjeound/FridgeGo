package com.stone.fridge.domain.use_case.my

import com.stone.fridge.core.util.Resource
import com.stone.fridge.domain.repository.MyRepository
import kotlinx.coroutines.flow.Flow
import java.io.File
import javax.inject.Inject

class UploadProfileImageUseCase @Inject constructor(
    private val myRepository: MyRepository
) {
    operator fun invoke(file: File): Flow<Resource<String>> {
        return myRepository.uploadProfileImage(file)
    }
}