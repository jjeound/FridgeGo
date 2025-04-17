package com.example.untitled_capstone.domain.use_case.app_entry

import com.example.untitled_capstone.domain.repository.LocalUserRepository
import javax.inject.Inject

class SaveAppEntry @Inject constructor(
    private val localUserRepository: LocalUserRepository
) {

    suspend operator fun invoke(){
        localUserRepository.saveAppEntry()
    }

}

