package com.stone.fridge.domain.use_case.token

import com.stone.fridge.core.util.Constants.NETWORK_ERROR
import com.stone.fridge.domain.repository.TokenRepository
import com.kakao.sdk.common.Constants.AUTHORIZATION
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.Protocol
import okhttp3.Request
import okhttp3.Response
import okhttp3.ResponseBody.Companion.toResponseBody
import javax.inject.Inject

class AuthInterceptor @Inject constructor(
    private val tokenManager: TokenRepository,
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val token: String? = runBlocking {
            tokenManager.getAccessToken()
        }

        if (token.isNullOrEmpty()) {
            return errorResponse(chain.request())
        }

        val request = chain.request().newBuilder().header(AUTHORIZATION, token).build()

        return chain.proceed(request)
    }

    private fun errorResponse(request: Request): Response {
        val emptyBody = "".toResponseBody("application/json".toMediaTypeOrNull())
        CoroutineScope(Dispatchers.IO).launch {
            tokenManager.refreshAndSaveToken()
        }
        return Response.Builder()
            .request(request)
            .protocol(Protocol.HTTP_2)
            .code(NETWORK_ERROR)
            .message("Unauthorized")
            .body(emptyBody)
            .build()
    }
}