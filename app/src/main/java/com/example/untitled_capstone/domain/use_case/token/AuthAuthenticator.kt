package com.example.untitled_capstone.domain.use_case.token

import android.util.Log
import com.example.untitled_capstone.data.remote.dto.TokenDto
import com.example.untitled_capstone.data.util.ErrorCode.JWT4004
import com.example.untitled_capstone.domain.repository.TokenRepository
import com.example.untitled_capstone.presentation.util.AuthEvent
import com.example.untitled_capstone.presentation.util.AuthEventBus
import com.kakao.sdk.common.Constants.AUTHORIZATION
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route
import javax.inject.Inject

class AuthAuthenticator @Inject constructor(
    private val tokenManager: TokenRepository
) : Authenticator {
    override fun authenticate(route: Route?, response: Response): Request? {
        if (responseCount(response) >= MAX_AUTH_RETRY) {
            return null // 무한 루프 방지
        }

        if (response.code != 401) return null

        val refreshToken = runBlocking {
            tokenManager.getRefreshToken().firstOrNull()
        }?: return null

        val newToken = runBlocking {
            withContext(Dispatchers.IO) { refreshAndSaveToken(refreshToken) }
        } ?: return null

        return response.request.newBuilder()
            .header(AUTHORIZATION, newToken.accessToken)
            .build()
    }

    private suspend fun refreshAndSaveToken(refreshToken: String): TokenDto? {
        val newToken = requestNewToken(refreshToken)
        newToken?.let {
            tokenManager.saveAccessToken(it.accessToken)
            tokenManager.saveRefreshToken(it.refreshToken)
        }
        return newToken
    }

    private suspend fun requestNewToken(refreshToken: String): TokenDto? {
        val response = tokenManager.refreshToken(refreshToken)
        Log.d("token", "response: ${response.message} ${response.data.toString()}")
        if (response.data == null || response.data.code == JWT4004) { // 리프레시 토큰도 만료됨
            handleTokenExpired()
            return null
        }
        return response.data.result
    }

    private fun responseCount(response: Response): Int {
        var count = 1
        var priorResponse = response.priorResponse
        while (priorResponse != null) {
            count++
            priorResponse = priorResponse.priorResponse
        }
        return count
    }

    @OptIn(DelicateCoroutinesApi::class)
    private suspend fun handleTokenExpired() {
        AuthEventBus.send(AuthEvent.Logout)
        tokenManager.deleteTokens()
    }

    companion object {
        private const val MAX_AUTH_RETRY = 3
    }
}