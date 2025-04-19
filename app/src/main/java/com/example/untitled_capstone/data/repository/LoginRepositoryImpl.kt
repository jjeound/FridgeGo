package com.example.untitled_capstone.data.repository


import android.content.Context
import androidx.datastore.preferences.core.edit
import com.example.untitled_capstone.core.util.PrefKeys.EMAIL
import com.example.untitled_capstone.core.util.PrefKeys.NICKNAME
import com.example.untitled_capstone.core.util.Resource
import com.example.untitled_capstone.data.remote.dto.KakaoAccessTokenRequest
import com.example.untitled_capstone.data.remote.dto.LocationDto
import com.example.untitled_capstone.data.remote.service.LoginApi
import com.example.untitled_capstone.data.remote.service.MapApi
import com.example.untitled_capstone.domain.model.AccountInfo
import com.example.untitled_capstone.domain.model.Address
import com.example.untitled_capstone.domain.repository.LoginRepository
import com.example.untitled_capstone.domain.repository.TokenRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.first
import okio.IOException
import retrofit2.HttpException
import javax.inject.Inject
import kotlin.toString

class LoginRepositoryImpl @Inject constructor(
    private val api: LoginApi,
    private val mapApi: MapApi,
    private val tokenRepository: TokenRepository,
    @ApplicationContext context: Context
): LoginRepository {
    val dataStore = context.dataStore

    override suspend fun kakaoLogin(accessToken: String): Resource<AccountInfo> {
        return try {
            Resource.Loading(data = null)
            val response = api.kakaoLogin(KakaoAccessTokenRequest(accessToken))
            if(response.isSuccess){
                tokenRepository.saveAccessToken(response.result!!.accessToken)
                tokenRepository.saveRefreshToken(response.result.refreshToken)
                dataStore.edit { prefs ->
                    prefs[NICKNAME] = response.result.nickname ?: ""
                    prefs[EMAIL] = response.result.email
                }
                Resource.Success(response.result.toAccountInfo())
            }else{
                Resource.Error(response.message)
            }
        } catch (e: IOException) {
            Resource.Error(e.toString())
        } catch (e: HttpException) {
            Resource.Error(e.toString())
        }
    }

    override suspend fun setNickname(nickname: String): Resource<String> {
        return try {
            Resource.Loading(data = null)
            val token = tokenRepository.getAccessToken().first()
            val response = api.setNickname(token = token?: "", nickname = nickname)
            if(response.isSuccess){
                dataStore.edit { prefs ->
                    prefs[NICKNAME] = nickname
                }
                Resource.Success(response.result)
            } else {
                Resource.Error(response.message)
            }
        } catch (e: IOException) {
            Resource.Error(e.toString())
        } catch (e: HttpException) {
            Resource.Error(e.toString())
        }
    }

    override suspend fun modifyNickname(nickname: String): Resource<String> {
        return try {
            Resource.Loading(data = null)
            val token = tokenRepository.getAccessToken().first()
            val response = api.modifyNickname(token?: "", nickname)
            if(response.isSuccess){
                dataStore.edit { prefs ->
                    prefs[NICKNAME] = nickname
                }
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

    override suspend fun getAddressByCoord(
        x: String,
        y: String
    ): Resource<Address> {
        return try {
            Resource.Loading(data = null)
            val response = mapApi.getRegion(x = x, y = y)
            if(response.documents?.isNotEmpty() == true){
                Resource.Success(response.documents[0].address.toAddress())
            }else{
                Resource.Error("No address found")
            }
        } catch (e: IOException) {
            Resource.Error(e.toString())
        } catch (e: HttpException) {
            Resource.Error(e.toString())
        }
    }

    override suspend fun setLocation(district: String, neighborhood: String): Resource<String> {
        return try {
            Resource.Loading(data = null)
            val token = tokenRepository.getAccessToken().first()
            val response = api.setLocation(token?: "", LocationDto(district, neighborhood))
            if(response.isSuccess){
                Resource.Success(response.result)
            }else{
                Resource.Error(response.message)
            }
        } catch (e: IOException) {
            Resource.Error(e.toString())
        } catch (e: HttpException) {
            Resource.Error(e.toString())
        }
    }

}