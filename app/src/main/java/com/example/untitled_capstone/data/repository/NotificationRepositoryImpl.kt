package com.example.untitled_capstone.data.repository

import com.example.untitled_capstone.core.util.Resource
import com.example.untitled_capstone.data.AppDispatchers
import com.example.untitled_capstone.data.Dispatcher
import com.example.untitled_capstone.data.local.remote.NotificationDao
import com.example.untitled_capstone.domain.model.Notification
import com.example.untitled_capstone.domain.repository.NotificationRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class NotificationRepositoryImpl @Inject constructor(
    private val dao: NotificationDao,
    @Dispatcher(AppDispatchers.IO) private val ioDispatcher: CoroutineDispatcher,
): NotificationRepository {
    override fun getAllNotifications(): Flow<Resource<List<Notification>>> = flow {
        emit(Resource.Loading())
        val notifications = dao.getAllNotifications()
        emit(Resource.Success(notifications.map { it.toDomain() }))
    }.flowOn(ioDispatcher)

    override suspend fun readNotification() {
        dao.updateAllIsRead()
    }

    override fun insertNotification(notification: Notification): Flow<Unit> = flow {
        dao.insert(notification.toEntity())
        emit(Unit)
    }.flowOn(ioDispatcher)
}