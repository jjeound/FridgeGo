package com.stone.fridge.domain.use_case.app_entry

import com.stone.fridge.domain.repository.LocalUserRepository
import javax.inject.Inject

class ReadAppEntry @Inject constructor(
    private val localUserRepository: LocalUserRepository
) {

    suspend operator fun invoke(): Boolean {
        return localUserRepository.readAppEntry()
    }

}