package com.example.untitled_capstone.domain.use_case.my

import com.example.untitled_capstone.domain.repository.TokenRepository
import javax.inject.Inject

class GetAccessToken @Inject constructor(
    private val tokenRepository: TokenRepository
){
    operator fun invoke() = tokenRepository.getAccessToken()
}