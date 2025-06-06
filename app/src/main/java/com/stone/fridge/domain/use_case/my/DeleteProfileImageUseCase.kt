package com.stone.fridge.domain.use_case.my

import com.stone.fridge.domain.repository.MyRepository
import javax.inject.Inject

class DeleteProfileImageUseCase @Inject constructor(
    private val repository: MyRepository
) {
    operator fun invoke() = repository.deleteProfileImage()
}