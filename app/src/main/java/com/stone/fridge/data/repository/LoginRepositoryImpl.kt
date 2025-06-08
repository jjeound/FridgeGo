package com.stone.fridge.data.repository


import android.content.Context
import androidx.annotation.WorkerThread
import androidx.datastore.preferences.core.edit
import com.stone.fridge.core.util.PrefKeys.DONG
import com.stone.fridge.core.util.PrefKeys.NICKNAME
import com.stone.fridge.core.util.PrefKeys.USER_ID
import com.stone.fridge.core.util.Resource
import com.stone.fridge.data.AppDispatchers
import com.stone.fridge.data.Dispatcher
import com.stone.fridge.data.remote.dto.KakaoAccessTokenRequest
import com.stone.fridge.data.remote.dto.LocationDto
import com.stone.fridge.data.remote.service.LoginApi
import com.stone.fridge.data.remote.service.MapApi
import com.stone.fridge.domain.model.AccountInfo
import com.stone.fridge.domain.model.Address
import com.stone.fridge.domain.repository.LoginRepository
import com.stone.fridge.domain.repository.TokenRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import okio.IOException
import org.json.JSONObject
import retrofit2.HttpException
import javax.inject.Inject
import kotlin.toString

class LoginRepositoryImpl @Inject constructor(
    private val api: LoginApi,
    private val mapApi: MapApi,
    private val tokenRepository: TokenRepository,
    @Dispatcher(AppDispatchers.IO) private val ioDispatcher: CoroutineDispatcher,
    @ApplicationContext private val context: Context,
): LoginRepository {
    val dataStore = context.dataStore

    @WorkerThread
    override fun kakaoLogin(accessToken: String): Flow<Resource<AccountInfo>> = flow {
        emit(Resource.Loading())
        try {
            val response = api.kakaoLogin(KakaoAccessTokenRequest(accessToken))
            //val response = api.loginTest(EmailReq("1"))
            if(response.isSuccess){
                tokenRepository.saveAccessToken(response.result!!.accessToken)
                tokenRepository.saveRefreshToken(response.result.refreshToken)
                saveUserId(userId = response.result.id)
                emit(Resource.Success(response.result.toAccountInfo()))
            }else{
                emit(Resource.Error(response.message))
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
    override fun setNickname(nickname: String): Flow<Resource<String>> = flow {
        emit(Resource.Loading())
        try {
            val token = tokenRepository.getAccessToken()
            val response = api.setNickname(token = token?: "", nickname = nickname)
            if(response.isSuccess){
                saveNickname(nickname)
                emit(Resource.Success(response.result))
            }else{
                emit(Resource.Error(response.message))
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

    private suspend fun saveNickname(nickname: String) {
        dataStore.edit { prefs ->
            prefs[NICKNAME] = nickname
        }
    }

    private suspend fun saveUserId(userId: Long) {
        dataStore.edit { prefs ->
            prefs[USER_ID] = userId
        }
    }

    @WorkerThread
    override fun getAddressByCoord(
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
    override fun setLocation(district: String, neighborhood: String): Flow<Resource<String>> = flow {
        emit(Resource.Loading())
        try {
            val token = tokenRepository.getAccessToken()
            val response = api.setLocation(token?: "", LocationDto(district, neighborhood))
            if(response.isSuccess){
                saveLocation(neighborhood)
                emit(Resource.Success(response.result))
            }else{
                emit(Resource.Error(response.message))
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

    private suspend fun saveLocation(neighborhood: String) {
        dataStore.edit { prefs ->
            prefs[DONG] = neighborhood
        }
    }
}