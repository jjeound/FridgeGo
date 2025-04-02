package com.example.untitled_capstone.data.repository

import android.content.Context
import android.util.Log
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.example.untitled_capstone.core.util.PrefKeys.EMAIL
import com.example.untitled_capstone.core.util.PrefKeys.IMAGE_URL
import com.example.untitled_capstone.core.util.PrefKeys.NICKNAME
import com.example.untitled_capstone.core.util.Resource
import com.example.untitled_capstone.data.remote.dto.ApiResponse
import com.example.untitled_capstone.data.remote.dto.ProfileDto
import com.example.untitled_capstone.data.remote.service.MyApi
import com.example.untitled_capstone.domain.model.Profile
import com.example.untitled_capstone.domain.repository.MyRepository
import com.example.untitled_capstone.domain.repository.TokenRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okio.IOException
import retrofit2.HttpException
import javax.inject.Inject

class MyRepositoryImpl @Inject constructor(
    private val api: MyApi,
    private val tokenRepository: TokenRepository,
    @ApplicationContext context: Context
): MyRepository {

    val dataStore = context.dataStore

    override suspend fun logout(): Resource<ApiResponse> {
        return try {
            Resource.Loading(data = null)
            val response = api.logout()
            if(response.isSuccess){
                tokenRepository.deleteTokens()
                Resource.Success(response)
            }else {
                Resource.Error(message = response.toString())
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
            val profile = getProfile()
            if(profile != null){
                return Resource.Success(profile)
            }else{
                val response = api.getProfile()
                if(response.isSuccess){
                    saveProfile(response.result!!)
                    Resource.Success(response.result.toProfile())
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
                Resource.Error(message = response.toString())
            }
        } catch (e: IOException) {
            Resource.Error(e.toString())
        } catch (e: HttpException) {
            Resource.Error(e.toString())
        }
    }

    override suspend fun uploadProfileImage(profileImage: MultipartBody.Part): Resource<ApiResponse> {
        return try {
            Resource.Loading(data = null)
            val response = api.uploadProfileImage(profileImage)
            if(response.isSuccess){
                Resource.Success(response)
            }else {
                Resource.Error(message = response.toString())
            }
        } catch (e: IOException) {
            Resource.Error(e.toString())
        } catch (e: HttpException) {
            Resource.Error(e.toString())
        }
    }

    private suspend fun getProfile(): Profile?{
        val nickname = dataStore.data.map { prefs ->
            prefs[NICKNAME]
        }.first()
        val email = dataStore.data.map { prefs ->
            prefs[EMAIL]
        }.first()
        val imgUrl = dataStore.data.map { prefs ->
            prefs[IMAGE_URL]
        }.first()
        if (nickname != null && email != null){
            return Profile(email, nickname, imgUrl)
        }
        return null
    }

    private suspend fun saveProfile(profile: ProfileDto){
        dataStore.edit { prefs ->
            prefs[NICKNAME] = profile.nickname ?: ""
        }
        dataStore.edit { prefs ->
            prefs[EMAIL] = profile.email
        }
        dataStore.edit { prefs ->
            prefs[IMAGE_URL] = profile.imageUrl?: ""
        }
    }
}