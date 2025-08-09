package com.stone.fridge.core.data.my

import android.content.Context
import androidx.annotation.WorkerThread
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import com.stone.fridge.core.auth.TokenDataSource
import com.stone.fridge.core.data.util.ImageCompressor
import com.stone.fridge.core.data.util.PrefKeys.NICKNAME
import com.stone.fridge.core.model.Profile
import com.stone.fridge.core.model.Report
import com.stone.fridge.core.network.AppDispatchers
import com.stone.fridge.core.network.Dispatcher
import com.stone.fridge.core.network.service.MyClient
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import javax.inject.Inject

class MyRepositoryImpl @Inject constructor(
    private val myClient: MyClient,
    private val tokenDataSource: TokenDataSource,
    private val dataStore: DataStore<Preferences>,
    @param:ApplicationContext private val context: Context,
    @param:Dispatcher(AppDispatchers.IO) private val ioDispatcher: CoroutineDispatcher,
): MyRepository {

    @WorkerThread
    override fun logout(): Flow<String> = flow {
        myClient.logout().result?.let {
            tokenDataSource.deleteTokens()
            emit(it)
        }
    }.flowOn(ioDispatcher)

    @WorkerThread
    override fun getMyProfile(): Flow<Profile> = flow {
        myClient.getProfile().result?.let {
            emit(it)
        }
    }.flowOn(ioDispatcher)

    @WorkerThread
    override fun getOtherProfile(nickname: String): Flow<Profile> = flow {
        myClient.getOtherProfile(nickname).result?.let {
            emit(it)
        }
    }.flowOn(ioDispatcher)

    @WorkerThread
    override fun uploadProfileImage(profileImage: File): Flow<String> = flow {
        val compressedFile = ImageCompressor.compressImage(context, profileImage)
        val requestFile = compressedFile.asRequestBody("image/*".toMediaTypeOrNull())
        val body = MultipartBody.Part.createFormData("profileImage", compressedFile.name, requestFile)
        myClient.uploadProfileImage(body).result?.let {
            emit(it)
        }
    }.flowOn(ioDispatcher)

    @WorkerThread
    override fun repostUser(
        targetUserId: Long,
        reportType: String,
        content: String
    ): Flow<String> = flow {
        myClient.reportUser(targetUserId, Report(
            reportType = reportType, content = content)).result?.let {
                emit(it)
        }
    }.flowOn(ioDispatcher)

    @WorkerThread
    override fun deleteProfileImage(): Flow<String> = flow {
        myClient.deleteProfileImage().result?.let {
            emit(it)
        }
    }.flowOn(ioDispatcher)

    @WorkerThread
    override fun modifyNickname(nickname: String): Flow<String> = flow{
        myClient.modifyNickname(nickname).result?.let {
            saveNickname(nickname)
            emit(it)
        }
    }.flowOn(ioDispatcher)

    @WorkerThread
    override fun deleteUser(): Flow<String> = flow{
        myClient.deleteUser().result?.let {
            tokenDataSource.deleteTokens()
            emit(it)
        }
    }.flowOn(ioDispatcher)

    private suspend fun saveNickname(nickname: String) {
        dataStore.edit { prefs ->
            prefs[NICKNAME] = nickname
        }
    }
}