package com.example.untitled_capstone.data.repository


import android.content.Context
import androidx.annotation.WorkerThread
import androidx.datastore.preferences.core.edit
import com.example.untitled_capstone.core.util.PrefKeys.NICKNAME
import com.example.untitled_capstone.core.util.Resource
import com.example.untitled_capstone.data.AppDispatchers
import com.example.untitled_capstone.data.Dispatcher
import com.example.untitled_capstone.data.local.db.ProfileDatabase
import com.example.untitled_capstone.data.local.entity.ProfileEntity
import com.example.untitled_capstone.data.remote.dto.EmailReq
import com.example.untitled_capstone.data.remote.dto.KakaoAccessTokenRequest
import com.example.untitled_capstone.data.remote.dto.LocationDto
import com.example.untitled_capstone.data.remote.service.LoginApi
import com.example.untitled_capstone.data.remote.service.MapApi
import com.example.untitled_capstone.domain.model.AccountInfo
import com.example.untitled_capstone.domain.model.Address
import com.example.untitled_capstone.domain.repository.LoginRepository
import com.example.untitled_capstone.domain.repository.TokenRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import okio.IOException
import retrofit2.HttpException
import javax.inject.Inject
import kotlin.toString

class LoginRepositoryImpl @Inject constructor(
    private val api: LoginApi,
    private val mapApi: MapApi,
    private val tokenRepository: TokenRepository,
    private val db: ProfileDatabase,
    @Dispatcher(AppDispatchers.IO) private val ioDispatcher: CoroutineDispatcher,
    @ApplicationContext private val context: Context,
): LoginRepository {
    val dataStore = context.dataStore

    @WorkerThread
    override suspend fun kakaoLogin(accessToken: String): Flow<Resource<AccountInfo>> = flow {
        emit(Resource.Loading())
        try {
            val response = api.kakaoLogin(KakaoAccessTokenRequest(accessToken))
            //val response = api.loginTest(EmailReq(accessToken))
            if(response.isSuccess){
                tokenRepository.saveAccessToken(response.result!!.accessToken)
                tokenRepository.saveRefreshToken(response.result.refreshToken)
                db.dao.insertProfile(
                    ProfileEntity(
                        id = response.result.id,
                        email = response.result.email,
                        nickname = response.result.nickname,
                    )
                )
                emit(Resource.Success(response.result.toAccountInfo()))
            }else{
                emit(Resource.Error(response.message))
            }
        } catch (e: IOException) {
            emit(Resource.Error(e.toString()))
        } catch (e: HttpException) {
            emit(Resource.Error(e.toString()))
        }
    }.flowOn(ioDispatcher)

    @WorkerThread
    override suspend fun setNickname(nickname: String): Flow<Resource<String>> = flow {
        emit(Resource.Loading())
        try {
            val token = tokenRepository.getAccessToken()
            val response = api.setNickname(token = token?: "", nickname = nickname)
            if(response.isSuccess){
                db.dao.updateNickname(
                    nickname = nickname
                )
                saveNickname(nickname)
                emit(Resource.Success(response.result))
            }else{
                emit(Resource.Error(response.message))
            }
        } catch (e: IOException) {
            emit(Resource.Error(e.toString()))
        } catch (e: HttpException) {
            emit(Resource.Error(e.toString()))
        }
    }.flowOn(ioDispatcher)

    @WorkerThread
    override suspend fun modifyNickname(nickname: String): Flow<Resource<String>> = flow{
        emit(Resource.Loading())
        try {
            val token = tokenRepository.getAccessToken()
            val response = api.modifyNickname(token?: "", nickname)
            if(response.isSuccess){
                db.dao.updateNickname(
                    nickname = nickname
                )
                saveNickname(nickname)
                emit(Resource.Success(response.result))
            }else{
                emit(Resource.Error(response.message))
            }
        } catch (e: IOException) {
            emit(Resource.Error(e.toString()))
        } catch (e: HttpException) {
            emit(Resource.Error(e.toString()))
        }
    }.flowOn(ioDispatcher)

    private suspend fun saveNickname(nickname: String) {
        dataStore.edit { prefs ->
            prefs[NICKNAME] = nickname
        }
    }

    @WorkerThread
    override suspend fun getAddressByCoord(
        x: String,
        y: String
    ): Flow<Resource<Address>> = flow {
        emit(Resource.Loading())
        try {
            val response = mapApi.getRegion(x = x, y = y)
            if(response.documents?.isNotEmpty() == true){
                emit(Resource.Success(response.documents[0].address.toAddress()))
            }else{
                emit(Resource.Error("No address found"))
            }
        } catch (e: IOException) {
            emit(Resource.Error(e.toString()))
        } catch (e: HttpException) {
            emit(Resource.Error(e.toString()))
        }
    }.flowOn(ioDispatcher)

    @WorkerThread
    override suspend fun setLocation(district: String, neighborhood: String): Flow<Resource<String>> = flow {
        emit(Resource.Loading())
        try {
            val token = tokenRepository.getAccessToken()
            val response = api.setLocation(token?: "", LocationDto(district, neighborhood))
            if(response.isSuccess){
                emit(Resource.Success(response.result))
            }else{
                emit(Resource.Error(response.message))
            }
        } catch (e: IOException) {
            emit(Resource.Error(e.toString()))
        } catch (e: HttpException) {
            emit(Resource.Error(e.toString()))
        }
    }.flowOn(ioDispatcher)

}