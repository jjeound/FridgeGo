package com.example.untitled_capstone.data.repository

import android.util.Log
import com.example.untitled_capstone.core.util.Resource
import com.example.untitled_capstone.data.remote.dto.KakaoLoginResponse
import com.example.untitled_capstone.data.remote.service.LoginApi
import com.example.untitled_capstone.domain.repository.KakaoLoginRepository
import okio.IOException
import retrofit2.HttpException


class KakaoLoginRepositoryImpl(
    private val api: LoginApi,
): KakaoLoginRepository {
    override suspend fun kakaoLoginCallback(code: String): Resource<KakaoLoginResponse> {
        return try {
            Resource.Loading(data = null)
            val response = api.kakaoLogin(code)
            Resource.Success(response)
        } catch (e: IOException) {
            Resource.Error(e.toString())
        } catch (e: HttpException) {
            Resource.Error(e.toString())
        }
    }

    override suspend fun whoami(): Resource<Unit> {
        return try {
            Resource.Loading(data = null)
            val response = api.whoami()
            Resource.Success(response)
        } catch (e: IOException) {
            Resource.Error(e.toString())
        } catch (e: HttpException) {
            Resource.Error(e.toString())
        }
    }


}