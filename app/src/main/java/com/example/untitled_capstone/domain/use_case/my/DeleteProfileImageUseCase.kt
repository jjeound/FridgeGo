package com.example.untitled_capstone.domain.use_case.my

import com.example.untitled_capstone.domain.repository.MyRepository
import javax.inject.Inject

class DeleteProfileImageUseCase @Inject constructor(
    private val repository: MyRepository
) {
    operator fun invoke() = repository.deleteProfileImage()
}