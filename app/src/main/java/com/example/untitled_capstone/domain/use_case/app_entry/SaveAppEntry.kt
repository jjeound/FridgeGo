package com.example.untitled_capstone.domain.use_case.app_entry

import com.example.untitled_capstone.domain.repository.LocalUserManger
import javax.inject.Inject

class SaveAppEntry @Inject constructor(
    private val localUserManger: LocalUserManger
) {

    suspend operator fun invoke(){
        localUserManger.saveAppEntry()
    }

}

