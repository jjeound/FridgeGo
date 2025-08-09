package com.stone.fridge.core.network.manager

import com.stone.fridge.core.auth.AuthEvent
import com.stone.fridge.core.auth.AuthEventBus
import com.stone.fridge.core.auth.TokenDataSource
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.Protocol
import okhttp3.Request
import okhttp3.Response
import okhttp3.ResponseBody.Companion.toResponseBody
import javax.inject.Inject

class AuthInterceptor @Inject constructor(
    private val tokenDataSource: TokenDataSource,
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val token: String? = runBlocking {
            tokenDataSource.getAccessToken()
        }

        if (token.isNullOrEmpty()) {
            return errorResponse(chain.request())
        }

        val request = chain.request().newBuilder().header("Authorization", token).build()

        return chain.proceed(request)
    }

    private fun errorResponse(request: Request): Response {
        val emptyBody = "".toResponseBody("application/json".toMediaTypeOrNull())
        AuthEventBus.send(AuthEvent.Logout)
        return Response.Builder()
            .request(request)
            .protocol(Protocol.HTTP_2)
            .code(401)
            .message("Unauthorized")
            .body(emptyBody)
            .build()
    }
}