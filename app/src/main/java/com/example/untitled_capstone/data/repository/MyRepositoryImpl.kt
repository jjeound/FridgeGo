package com.example.untitled_capstone.data.repository

import android.content.Context
import com.example.untitled_capstone.core.util.PrefKeys.NICKNAME
import com.example.untitled_capstone.core.util.Resource
import com.example.untitled_capstone.data.local.remote.ProfileDao
import com.example.untitled_capstone.data.remote.dto.ReportDto
import com.example.untitled_capstone.data.remote.service.MyApi
import com.example.untitled_capstone.domain.model.Profile
import com.example.untitled_capstone.domain.repository.MyRepository
import com.example.untitled_capstone.domain.repository.TokenRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import okhttp3.MultipartBody
import okio.IOException
import retrofit2.HttpException
import javax.inject.Inject

class MyRepositoryImpl @Inject constructor(
    private val api: MyApi,
    private val tokenRepository: TokenRepository,
    private val dao: ProfileDao,
    @ApplicationContext private val context: Context
): MyRepository {
    val dataStore = context.dataStore

    override suspend fun logout(): Resource<String> {
        return try {
            Resource.Loading(data = null)
            val response = api.logout()
            if(response.isSuccess){
                tokenRepository.deleteTokens()
                Resource.Success(response.result)
            }else {
                Resource.Error(message = response.message)
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
            val profile = dao.getProfile()
            if(profile != null){
                return Resource.Success(profile.toProfile())
            }else{
                val response = api.getProfile()
                if(response.isSuccess){
                    Resource.Success(response.result!!.toProfile())
                } else {
                    Resource.Error(response.message)
                }
            }
        } catch (e: IOException) {
            Resource.Error(e.toString())
        } catch (e: HttpException) {
            Resource.Error(e.toString())
        }
    }

    override suspend fun getNickname(): Flow<String> {
        return dataStore.data.map { prefs ->
            prefs[NICKNAME] ?: "User"
        }
    }

    override suspend fun getOtherProfile(nickname: String): Resource<Profile> {
        return try {
            Resource.Loading(data = null)
            val response = api.getOtherProfile(nickname)
            if(response.isSuccess){
                Resource.Success(response.result!!.toProfile())
            } else {
                Resource.Error(response.message)
            }
        } catch (e: IOException) {
            Resource.Error(e.toString())
        } catch (e: HttpException) {
            Resource.Error(e.toString())
        }
    }

    override suspend fun getLocation(): Resource<String> {
        return try {
            Resource.Loading(data = null)
            val response = api.getLocation()
            if(response.isSuccess){
                Resource.Success(response.result!!.neighborhood)
            }else {
                Resource.Error(message = response.message)
            }
        } catch (e: IOException) {
            Resource.Error(e.toString())
        } catch (e: HttpException) {
            Resource.Error(e.toString())
        }
    }

    override suspend fun uploadProfileImage(profileImage: MultipartBody.Part): Resource<String> {
        return try {
            Resource.Loading(data = null)
            val response = api.uploadProfileImage(profileImage)
            if(response.isSuccess){
                Resource.Success(response.result)
            }else {
                Resource.Error(message = response.message)
            }
        } catch (e: IOException) {
            Resource.Error(e.toString())
        } catch (e: HttpException) {
            Resource.Error(e.toString())
        }
    }

    override suspend fun repostUser(
        targetUserId: Long,
        reportType: String,
        content: String
    ): Resource<String> {
        return try {
            Resource.Loading(data = null)
            val response = api.reportUser(targetUserId, ReportDto(reportType, content))
            if(response.isSuccess){
                Resource.Success(response.result)
            }else {
                Resource.Error(message = response.message)
            }
        } catch (e: IOException) {
            Resource.Error(e.toString())
        } catch (e: HttpException) {
            Resource.Error(e.toString())
        }
    }
}