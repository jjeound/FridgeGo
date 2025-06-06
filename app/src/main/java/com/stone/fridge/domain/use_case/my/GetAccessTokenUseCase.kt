package com.stone.fridge.domain.use_case.my

import com.stone.fridge.domain.repository.TokenRepository
import javax.inject.Inject

class GetAccessTokenUseCase @Inject constructor(
    private val tokenRepository: TokenRepository
){
    suspend operator fun invoke() = tokenRepository.getAccessToken()
}