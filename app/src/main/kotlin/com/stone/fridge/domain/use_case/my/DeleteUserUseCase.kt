package com.stone.fridge.domain.use_case.my

import com.stone.fridge.domain.repository.MyRepository
import javax.inject.Inject

class DeleteUserUseCase @Inject constructor(
    private val repository: MyRepository
) {
    operator fun invoke() = repository.deleteUser()
}