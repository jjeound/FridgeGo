package com.example.untitled_capstone.data.repository

import android.content.Context
import android.util.Log
import androidx.annotation.WorkerThread
import androidx.datastore.preferences.core.edit
import com.example.untitled_capstone.core.util.PrefKeys.DONG
import com.example.untitled_capstone.core.util.PrefKeys.NICKNAME
import com.example.untitled_capstone.core.util.Resource
import com.example.untitled_capstone.data.AppDispatchers
import com.example.untitled_capstone.data.Dispatcher
import com.example.untitled_capstone.data.remote.dto.ReportDto
import com.example.untitled_capstone.data.remote.service.MyApi
import com.example.untitled_capstone.data.util.ImageCompressor
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
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okio.IOException
import org.json.JSONObject
import retrofit2.HttpException
import java.io.File
import javax.inject.Inject

class MyRepositoryImpl @Inject constructor(
    private val api: MyApi,
    private val tokenRepository: TokenRepository,
    @ApplicationContext private val context: Context,
    @Dispatcher(AppDispatchers.IO) private val ioDispatcher: CoroutineDispatcher,
): MyRepository {
    val dataStore = context.dataStore

    @WorkerThread
    override fun logout(): Flow<Resource<String>> = flow {
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
            val errorMessage = try {
                val errorJson = e.response()?.errorBody()?.string()
                val errorObj = JSONObject(errorJson ?: "")
                errorObj.getString("message")
            } catch (_: Exception) {
                "알 수 없는 오류가 발생했어요."
            }
            emit(Resource.Error(errorMessage))
        }
    }.flowOn(ioDispatcher)

    @WorkerThread
    override fun getMyProfile(): Flow<Resource<Profile>> = flow {
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
            val errorMessage = try {
                val errorJson = e.response()?.errorBody()?.string()
                val errorObj = JSONObject(errorJson ?: "")
                errorObj.getString("message")
            } catch (_: Exception) {
                "알 수 없는 오류가 발생했어요."
            }
            emit(Resource.Error(errorMessage))
        }
    }.flowOn(ioDispatcher)

    override suspend fun getNickname(): String? {
        val name = dataStore.data.map { prefs ->
            prefs[NICKNAME]
        }.first()
        if (name == null){
            val response = api.getProfile()
            if (response.isSuccess){
                dataStore.edit { prefs ->
                    prefs[NICKNAME] = response.result!!.nickname!!
                }
                return response.result!!.nickname
            } else{
                Log.d("getNickname", "Failed to fetch nickname: ${response.message}")
                return null
            }
        } else {
            return name
        }
    }

    @WorkerThread
    override fun getOtherProfile(nickname: String): Flow<Resource<Profile>> = flow {
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
            val errorMessage = try {
                val errorJson = e.response()?.errorBody()?.string()
                val errorObj = JSONObject(errorJson ?: "")
                errorObj.getString("message")
            } catch (_: Exception) {
                "알 수 없는 오류가 발생했어요."
            }
            emit(Resource.Error(errorMessage))
        }
    }.flowOn(ioDispatcher)

    @WorkerThread
    override fun getLocation(): Flow<Resource<String>> = flow {
        emit(Resource.Loading())
        val dong = dataStore.data.map { prefs ->
            prefs[DONG]
        }.first()
        if (dong == null){
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
        }else {
            emit(Resource.Success(dong))
        }
    }.flowOn(ioDispatcher)

    @WorkerThread
    override fun uploadProfileImage(profileImage: File): Flow<Resource<String>> = flow {
        emit(Resource.Loading())
        val compressedFile = ImageCompressor.compressImage(context, profileImage)
        val requestFile = compressedFile.asRequestBody("image/*".toMediaTypeOrNull())
        val body = MultipartBody.Part.createFormData("profileImage", compressedFile.name, requestFile)
        try {
            val response = api.uploadProfileImage(body)
            if(response.isSuccess){
                emit(Resource.Success(response.result))
            }else {
                emit(Resource.Error(message = response.message))
            }
        } catch (e: IOException) {
            emit(Resource.Error(e.toString()))
        } catch (e: HttpException) {
            val errorMessage = try {
                val errorJson = e.response()?.errorBody()?.string()
                val errorObj = JSONObject(errorJson ?: "")
                errorObj.getString("message")
            } catch (_: Exception) {
                "알 수 없는 오류가 발생했어요."
            }
            emit(Resource.Error(errorMessage))
        }
    }.flowOn(ioDispatcher)

    @WorkerThread
    override fun repostUser(
        targetUserId: Long,
        reportType: String,
        content: String
    ): Flow<Resource<String>> = flow {
        emit(Resource.Loading())
        try {
            val response = api.reportUser(targetUserId, ReportDto(
                reportType = reportType, content = content))
            if(response.isSuccess){
                emit(Resource.Success(response.result))
            }else {
                emit(Resource.Error(message = response.message))
            }
        } catch (e: IOException) {
            emit(Resource.Error(e.toString()))
        } catch (e: HttpException) {
            val errorMessage = try {
                val errorJson = e.response()?.errorBody()?.string()
                val errorObj = JSONObject(errorJson ?: "")
                errorObj.getString("message")
            } catch (_: Exception) {
                "알 수 없는 오류가 발생했어요."
            }
            emit(Resource.Error(errorMessage))
        }
    }.flowOn(ioDispatcher)
}