package com.example.untitled_capstone.domain.use_case.login

import com.example.untitled_capstone.domain.repository.FCMRepository
import javax.inject.Inject


class SaveFCMTokenUseCase @Inject constructor(
    private val repository: FCMRepository
) {
    suspend operator fun invoke(token: String){
        return repository.saveFcmToken(token)
    }
}