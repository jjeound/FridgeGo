package com.stone.fridge.core.data.notification

import android.util.Log
import androidx.annotation.WorkerThread
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import com.stone.fridge.core.data.util.PrefKeys.IS_UNREAD_NOTIFICATION
import com.stone.fridge.core.model.Notification
import com.stone.fridge.core.network.AppDispatchers
import com.stone.fridge.core.network.Dispatcher
import com.stone.fridge.core.network.service.NotificationClient
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject

class NotificationRepositoryImpl @Inject constructor(
    private val notificationClient: NotificationClient,
    private val dataStore: DataStore<Preferences>,
    @param:Dispatcher(AppDispatchers.IO) private val ioDispatcher: CoroutineDispatcher,
): NotificationRepository {

    @WorkerThread
    override fun getAllNotifications(): Flow<List<Notification>> = flow {
        notificationClient.getNotifications().result?.let {
            readNotification()
            emit(it)
        }
    }.flowOn(ioDispatcher)

    @WorkerThread
    override suspend fun readNotification() {
        withContext(ioDispatcher){
            try {
                notificationClient.readAll().result?.let {
                    saveUnreadCount(false)
                }
            } catch (e: Exception){
                Log.d("NotificationRepository", "readNotification: ${e.message}")
                throw e
            }
        }
    }


    @WorkerThread
    override fun getUnreadCount(): Flow<Long> = flow {
        notificationClient.getUnreadCount().result?.let {
            if(it>0){
                saveUnreadCount(true)
            } else {
                saveUnreadCount(false)
            }
            emit(it)
        }
    }.flowOn(ioDispatcher)

    override fun isUnreadNotification(): Flow<Boolean> {
        return dataStore.data.map { prefs ->
            prefs[IS_UNREAD_NOTIFICATION] == true
        }
    }

    private suspend fun saveUnreadCount(isExist: Boolean){
        dataStore.edit { prefs ->
            prefs[IS_UNREAD_NOTIFICATION] = isExist
        }
    }
}