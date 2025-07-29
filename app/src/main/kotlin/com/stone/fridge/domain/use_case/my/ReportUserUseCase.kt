package com.stone.fridge.domain.use_case.my

import com.stone.fridge.core.util.Resource
import com.stone.fridge.domain.repository.MyRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ReportUserUseCase @Inject constructor(
    private val myRepository: MyRepository
) {
    operator fun invoke(targetUserId: Long, reportType: String, content: String): Flow<Resource<String>> {
        return myRepository.repostUser(targetUserId, reportType, content)
    }
}