package com.stone.fridge.core.network.manager

import android.util.Log
import com.stone.fridge.core.auth.AuthEvent
import com.stone.fridge.core.auth.AuthEventBus
import com.stone.fridge.core.auth.TokenDataSource
import com.stone.fridge.core.network.AppDispatchers
import com.stone.fridge.core.network.Dispatcher
import com.stone.fridge.core.network.service.TokenClient
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class AuthAuthenticator @Inject constructor(
    private val tokenDataSource: TokenDataSource,
    private val tokenClient: TokenClient,
    @param:Dispatcher(AppDispatchers.IO) private val ioDispatcher: CoroutineDispatcher
) : Authenticator {
    override fun authenticate(route: Route?, response: Response): Request? {
        if (responseCount(response) >= MAX_AUTH_RETRY) return null
        if (response.code != 401) return null

        val newToken = runBlocking(ioDispatcher) {
            val refreshToken = tokenDataSource.getRefreshToken() ?: return@runBlocking null
            val mutex = Mutex()
            try {
                val response = mutex.withLock {tokenClient.refreshToken(refreshToken)}
                if (response.isSuccess) {
                    tokenDataSource.saveAccessToken(response.result!!.accessToken)
                    tokenDataSource.saveRefreshToken(response.result.refreshToken)
                    Log.e("AuthAuthenticator", "Refresh token success")
                    response.result.accessToken
                } else {
                    null
                }
            } catch (e: HttpException) {
                if (e.code() == 401) {
                    Log.e("AuthAuthenticator", e.message.toString())
                    AuthEventBus.send(AuthEvent.Logout)
                    tokenDataSource.deleteTokens()
                }
                null
            } catch (e: IOException) {
                Log.e("AuthAuthenticator", e.message.toString())
                null
            }
        }

        return newToken?.let {
            response.request.newBuilder()
                .header("Authorization",  it)
                .build()
        }
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

    companion object {
        private const val MAX_AUTH_RETRY = 3
    }
}