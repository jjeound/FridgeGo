package com.example.untitled_capstone.domain.use_case.token

import com.example.untitled_capstone.core.util.Constants.NETWORK_ERROR
import com.example.untitled_capstone.domain.repository.TokenRepository
import com.kakao.sdk.common.Constants.AUTHORIZATION
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.Protocol
import okhttp3.Request
import okhttp3.Response
import okhttp3.ResponseBody.Companion.toResponseBody
import timber.log.Timber
import java.net.HttpURLConnection.HTTP_OK
import javax.inject.Inject

class AuthInterceptor @Inject constructor(
    private val tokenManager: TokenRepository
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val token: String? = runBlocking {
            tokenManager.getAccessToken().firstOrNull()
        }

        if (token.isNullOrEmpty()) {
            return errorResponse(chain.request())
        }

        val request = chain.request().newBuilder().header(AUTHORIZATION, "Bearer $token").build()

        val response = chain.proceed(request)
        if (response.code == HTTP_OK) {
            val newAccessToken: String = response.header(AUTHORIZATION, null) ?: return response
            Timber.d("new Access Token: $newAccessToken")

            runBlocking {
                withContext(Dispatchers.IO) {
                    val existedAccessToken = tokenManager.getAccessToken().first()
                    if (existedAccessToken != newAccessToken) {
                        tokenManager.saveAccessToken(newAccessToken)
                    }
                }
            }
        } else {
            Timber.e("${response.code} : ${response.request} \n ${response.message}")
        }

        return response
    }

    private fun errorResponse(request: Request): Response {
        val emptyBody = "".toResponseBody("application/json".toMediaTypeOrNull())
        return Response.Builder()
            .request(request)
            .protocol(Protocol.HTTP_2)
            .code(NETWORK_ERROR)
            .message("Unauthorized")
            .body(emptyBody)
            .build()
    }
}