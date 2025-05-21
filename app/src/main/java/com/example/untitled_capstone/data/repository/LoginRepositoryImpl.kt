package com.example.untitled_capstone.data.repository


import android.content.Context
import android.util.Log
import androidx.datastore.preferences.core.edit
import com.example.untitled_capstone.core.util.PrefKeys.NICKNAME
import com.example.untitled_capstone.core.util.Resource
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
import okio.IOException
import retrofit2.HttpException
import javax.inject.Inject
import kotlin.toString

class LoginRepositoryImpl @Inject constructor(
    private val api: LoginApi,
    private val mapApi: MapApi,
    private val tokenRepository: TokenRepository,
    private val db: ProfileDatabase,
    @ApplicationContext private val context: Context
): LoginRepository {
    val dataStore = context.dataStore

    override suspend fun kakaoLogin(accessToken: String): Resource<AccountInfo> {
        return try {
            Resource.Loading(data = null)
            //val response = api.kakaoLogin(KakaoAccessTokenRequest(accessToken))
            val response = api.loginTest(EmailReq(accessToken))
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
            val token = tokenRepository.getAccessToken()
            val response = api.setNickname(token = token?: "", nickname = nickname)
            if(response.isSuccess){
                db.dao.updateNickname(
                    nickname = nickname
                )
                saveNickname(nickname)
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
            val token = tokenRepository.getAccessToken()
            val response = api.modifyNickname(token?: "", nickname)
            if(response.isSuccess){
                db.dao.updateNickname(
                    nickname = nickname
                )
                saveNickname(nickname)
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

    private suspend fun saveNickname(nickname: String) {
        dataStore.edit { prefs ->
            prefs[NICKNAME] = nickname
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
            val token = tokenRepository.getAccessToken()
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