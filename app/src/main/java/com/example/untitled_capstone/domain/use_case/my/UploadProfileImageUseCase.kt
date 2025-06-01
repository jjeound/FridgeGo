package com.example.untitled_capstone.domain.use_case.my

import com.example.untitled_capstone.core.util.Resource
import com.example.untitled_capstone.domain.repository.MyRepository
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