package com.stone.fridge.domain.use_case.my

import com.stone.fridge.core.util.Resource
import com.stone.fridge.domain.repository.MyRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ModifyNicknameUseCase @Inject constructor(
    private val repository: MyRepository
) {
    operator fun invoke(nickname: String): Flow<Resource<String>> {
        return repository.modifyNickname(nickname)
    }
}