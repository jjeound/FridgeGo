package com.stone.fridge.domain.use_case.my

import com.stone.fridge.core.util.Resource
import com.stone.fridge.domain.model.Profile
import com.stone.fridge.domain.repository.MyRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetOtherProfileUseCase @Inject constructor(
    private val myRepository: MyRepository
) {
    operator fun invoke(nickname: String): Flow<Resource<Profile>> {
        return myRepository.getOtherProfile(nickname)
    }
}