package com.example.untitled_capstone.data.repository

import com.example.untitled_capstone.core.util.Resource
import com.example.untitled_capstone.data.remote.dto.KakaoAccessTokenRequest
import com.example.untitled_capstone.data.remote.dto.LoginCallbackResponse
import com.example.untitled_capstone.data.remote.service.LoginApi
import com.example.untitled_capstone.domain.repository.LoginRepository
import okio.IOException
import retrofit2.HttpException


class LoginRepositoryImpl(
    private val api: LoginApi,
): LoginRepository {
    override suspend fun loginCallback(accessToken: KakaoAccessTokenRequest): Resource<LoginCallbackResponse> {
        return try {
            Resource.Loading(data = null)
            val response = api.kakaoLogin(accessToken)
            Resource.Success(response)
        } catch (e: IOException) {
            Resource.Error(e.toString())
        } catch (e: HttpException) {
            Resource.Error(e.toString())
        }
    }
}