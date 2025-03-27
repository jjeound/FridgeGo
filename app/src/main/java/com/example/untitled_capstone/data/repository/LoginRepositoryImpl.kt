package com.example.untitled_capstone.data.repository


import android.util.Log
import com.example.untitled_capstone.core.util.Resource
import com.example.untitled_capstone.data.remote.dto.ApiResponse
import com.example.untitled_capstone.data.remote.dto.KakaoAccessTokenRequest
import com.example.untitled_capstone.data.remote.service.LoginApi
import com.example.untitled_capstone.domain.model.AccountInfo
import com.example.untitled_capstone.domain.repository.LoginRepository
import com.example.untitled_capstone.domain.repository.TokenRepository
import kotlinx.coroutines.flow.first
import okio.IOException
import retrofit2.HttpException
import javax.inject.Inject

class LoginRepositoryImpl @Inject constructor(
    private val api: LoginApi,
    private val tokenRepository: TokenRepository
): LoginRepository {

    override suspend fun kakaoLogin(accessToken: KakaoAccessTokenRequest): Resource<AccountInfo> {
        return try {
            Resource.Loading(data = null)
            val response = api.kakaoLogin(accessToken)
            if(response.isSuccess){
                tokenRepository.saveAccessToken(response.result.accessToken)
                tokenRepository.saveRefreshToken(response.result.refreshToken)
                Resource.Success(response.result.toAccountInfo())
            }else{
                Log.d("login failed", response.toString())
                Resource.Error(response.message)
            }
        } catch (e: IOException) {
            Resource.Error(e.toString())
        } catch (e: HttpException) {
            Resource.Error(e.toString())
        }
    }

    override suspend fun setNickname(nickname: String): Resource<ApiResponse> {
        return try {
            Resource.Loading(data = null)
            val token = tokenRepository.getAccessToken().first()
            val response = api.setNickname(token = token!!, nickname = nickname)
            if(response.isSuccess){
                Resource.Success(response)
            } else {
                Resource.Error(response.message)
            }
        } catch (e: IOException) {
            Resource.Error(e.toString())
        } catch (e: HttpException) {
            Resource.Error(e.toString())
        }
    }

}