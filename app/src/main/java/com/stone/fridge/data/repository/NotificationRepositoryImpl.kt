package com.stone.fridge.data.repository

import android.content.Context
import androidx.annotation.WorkerThread
import androidx.datastore.preferences.core.edit
import com.stone.fridge.core.util.PrefKeys.IS_UNREAD_NOTIFICATION
import com.stone.fridge.core.util.Resource
import com.stone.fridge.data.AppDispatchers
import com.stone.fridge.data.Dispatcher
import com.stone.fridge.data.remote.service.NotificationApi
import com.stone.fridge.domain.model.Notification
import com.stone.fridge.domain.repository.NotificationRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import okio.IOException
import org.json.JSONObject
import retrofit2.HttpException
import javax.inject.Inject

class NotificationRepositoryImpl @Inject constructor(
    private val api: NotificationApi,
    @ApplicationContext private val context: Context,
    @Dispatcher(AppDispatchers.IO) private val ioDispatcher: CoroutineDispatcher,
): NotificationRepository {

    @WorkerThread
    override fun getAllNotifications(): Flow<Resource<List<Notification>>> = flow {
        emit(Resource.Loading())
        try {
            val response = api.getNotifications()
            if(response.isSuccess){
                emit(Resource.Success(response.result?.map { it.toDomain() }))
                readNotification()
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
    override fun readNotification(): Flow<Unit> = flow {
        try {
            val response = api.readAll()
            if(!response.isSuccess){
                saveUnreadCount(false) // 모든 알림을 불러오면 읽음 처리
                emit(Unit)
            }
        } catch (_: IOException) {
            api.readAll()
        } catch (_: HttpException) {
            api.readAll()
        }
    }.flowOn(ioDispatcher)


    @WorkerThread
    override fun getUnreadCount(): Flow<Resource<Long>> = flow {
        emit(Resource.Loading())
        try {
            val response = api.getUnreadCount()
            if(response.isSuccess){
                emit(Resource.Success(response.result))
                response.result?.let {
                    if(it>0){
                        saveUnreadCount(true)
                    } else {
                        saveUnreadCount(false)
                    }
                }
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

    override fun isUnreadNotification(): Flow<Boolean> {
        return context.dataStore.data.map { prefs ->
            prefs[IS_UNREAD_NOTIFICATION] == true
        }
    }

    private suspend fun saveUnreadCount(isExist: Boolean){
        context.dataStore.edit { prefs ->
            prefs[IS_UNREAD_NOTIFICATION] = isExist
        }
    }
}