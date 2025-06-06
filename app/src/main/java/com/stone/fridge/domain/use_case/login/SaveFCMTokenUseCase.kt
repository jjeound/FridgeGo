package com.stone.fridge.domain.use_case.login

import com.stone.fridge.domain.repository.FCMRepository
import javax.inject.Inject


class SaveFCMTokenUseCase @Inject constructor(
    private val repository: FCMRepository
) {
    suspend operator fun invoke(token: String){
        return repository.saveFcmToken(token)
    }
}