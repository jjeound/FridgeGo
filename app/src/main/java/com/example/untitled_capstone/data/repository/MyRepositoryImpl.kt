package com.example.untitled_capstone.data.repository

import com.example.untitled_capstone.core.util.Resource
import com.example.untitled_capstone.data.remote.dto.ApiResponse
import com.example.untitled_capstone.data.remote.service.MyApi
import com.example.untitled_capstone.domain.model.Profile
import com.example.untitled_capstone.domain.repository.MyRepository
import com.example.untitled_capstone.domain.repository.TokenRepository
import okio.IOException
import retrofit2.HttpException
import javax.inject.Inject

class MyRepositoryImpl @Inject constructor(
    private val api: MyApi,
    private val tokenRepository: TokenRepository
): MyRepository {
    override suspend fun logout(): Resource<ApiResponse> {
        return try {
            Resource.Loading(data = null)
            val response = api.logout()
            if(response.isSuccess){
                tokenRepository.deleteTokens()
                Resource.Success(response)
            }else{
                Resource.Error(response.message)
            }
        } catch (e: IOException) {
            Resource.Error(e.toString())
        } catch (e: HttpException) {
            Resource.Error(e.toString())
        }
    }

    override suspend fun getMyProfile(): Resource<Profile> {
        return try {
            Resource.Loading(data = null)
            val response = api.getProfile()
            if(response.isSuccess){
                Resource.Success(response.result.toProfile())
            } else {
                Resource.Error(response.message)
            }
        } catch (e: IOException) {
            Resource.Error(e.toString())
        } catch (e: HttpException) {
            Resource.Error(e.toString())
        }
    }

    override suspend fun getOtherProfile(nickname: String): Resource<Profile> {
        return try {
            Resource.Loading(data = null)
            val response = api.getOtherProfile(nickname)
            if(response.isSuccess){
                Resource.Success(response.result.toProfile())
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