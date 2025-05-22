package com.example.untitled_capstone.data.repository

import android.content.Context
import android.util.Log
import androidx.annotation.WorkerThread
import androidx.datastore.preferences.core.edit
import com.example.untitled_capstone.core.util.PrefKeys.MY_PROFILE
import com.example.untitled_capstone.core.util.PrefKeys.NICKNAME
import com.example.untitled_capstone.core.util.Resource
import com.example.untitled_capstone.data.AppDispatchers
import com.example.untitled_capstone.data.Dispatcher
import com.example.untitled_capstone.data.local.remote.ProfileDao
import com.example.untitled_capstone.data.remote.dto.ReportDto
import com.example.untitled_capstone.data.remote.service.MyApi
import com.example.untitled_capstone.domain.model.Profile
import com.example.untitled_capstone.domain.repository.MyRepository
import com.example.untitled_capstone.domain.repository.TokenRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import okhttp3.MultipartBody
import okio.IOException
import retrofit2.HttpException
import javax.inject.Inject

class MyRepositoryImpl @Inject constructor(
    private val api: MyApi,
    private val tokenRepository: TokenRepository,
    private val dao: ProfileDao,
    @ApplicationContext private val context: Context,
    @Dispatcher(AppDispatchers.IO) private val ioDispatcher: CoroutineDispatcher,
): MyRepository {
    val dataStore = context.dataStore

    @WorkerThread
    override suspend fun logout(): Flow<Resource<String>> = flow {
        emit(Resource.Loading())
        try {
            val response = api.logout()
            if(response.isSuccess){
                tokenRepository.deleteTokens()
                emit(Resource.Success(response.result))
            }else {
                emit(Resource.Error(message = response.message))
            }
        } catch (e: IOException) {
            emit(Resource.Error(e.toString()))
        } catch (e: HttpException) {
            emit(Resource.Error(e.toString()))
        }
    }.flowOn(ioDispatcher)

    @WorkerThread
    override suspend fun getMyProfile(): Flow<Resource<Profile>> = flow {
        emit(Resource.Loading())
        try {
            val response = api.getProfile()
            if(response.isSuccess){
                emit(Resource.Success(response.result!!.toProfile()))
            }else {
                emit(Resource.Error(message = response.message))
            }
        } catch (e: IOException) {
            emit(Resource.Error(e.toString()))
        } catch (e: HttpException) {
            emit(Resource.Error(e.toString()))
        }
    }.flowOn(ioDispatcher)

    override suspend fun getNickname(): String? {
        return dataStore.data.map { prefs ->
            prefs[NICKNAME]
        }.first()
    }

    @WorkerThread
    override suspend fun getOtherProfile(nickname: String): Flow<Resource<Profile>> = flow {
        emit(Resource.Loading())
        try {
            val response = api.getOtherProfile(nickname)
            if(response.isSuccess){
                emit(Resource.Success(response.result!!.toProfile()))
            }else {
                emit(Resource.Error(message = response.message))
            }
        } catch (e: IOException) {
            emit(Resource.Error(e.toString()))
        } catch (e: HttpException) {
            emit(Resource.Error(e.toString()))
        }
    }.flowOn(ioDispatcher)

    @WorkerThread
    override suspend fun getLocation(): Flow<Resource<String>> = flow {
        emit(Resource.Loading())
        try {
            val response = api.getLocation()
            if(response.isSuccess){
                emit(Resource.Success(response.result!!.neighborhood))
            }else {
                emit(Resource.Error(message = response.message))
            }
        } catch (e: IOException) {
            emit(Resource.Error(e.toString()))
        } catch (e: HttpException) {
            emit(Resource.Error(e.toString()))
        }
    }.flowOn(ioDispatcher)

    @WorkerThread
    override suspend fun uploadProfileImage(profileImage: MultipartBody.Part): Flow<Resource<String>> = flow {
        emit(Resource.Loading())
        try {
            val response = api.uploadProfileImage(profileImage)
            if(response.isSuccess){
                emit(Resource.Success(response.result))
            }else {
                emit(Resource.Error(message = response.message))
            }
        } catch (e: IOException) {
            emit(Resource.Error(e.toString()))
        } catch (e: HttpException) {
            emit(Resource.Error(e.toString()))
        }
    }.flowOn(ioDispatcher)

    @WorkerThread
    override suspend fun repostUser(
        targetUserId: Long,
        reportType: String,
        content: String
    ): Flow<Resource<String>> = flow {
        emit(Resource.Loading())
        try {
            val response = api.reportUser(targetUserId, ReportDto(reportType, content))
            if(response.isSuccess){
                emit(Resource.Success(response.result))
            }else {
                emit(Resource.Error(message = response.message))
            }
        } catch (e: IOException) {
            emit(Resource.Error(e.toString()))
        } catch (e: HttpException) {
            emit(Resource.Error(e.toString()))
        }
    }.flowOn(ioDispatcher)
}