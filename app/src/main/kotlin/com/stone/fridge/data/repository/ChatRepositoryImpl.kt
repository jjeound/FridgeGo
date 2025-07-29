package com.stone.fridge.data.repository

import android.content.Context
import androidx.annotation.WorkerThread
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.stone.fridge.core.util.Constants.MESSAGE_PAGE_SIZE
import com.stone.fridge.core.util.PrefKeys.NICKNAME
import com.stone.fridge.core.util.PrefKeys.USER_ID
import com.stone.fridge.core.util.Resource
import com.stone.fridge.data.AppDispatchers
import com.stone.fridge.data.Dispatcher
import com.stone.fridge.data.local.db.MessageItemDatabase
import com.stone.fridge.data.local.entity.MessageItemEntity
import com.stone.fridge.data.pagination.MessagePagingSource
import com.stone.fridge.data.remote.service.ChatApi
import com.stone.fridge.domain.model.ChatMember
import com.stone.fridge.domain.model.ChattingRoom
import com.stone.fridge.domain.model.ChattingRoomRaw
import com.stone.fridge.domain.model.Message
import com.stone.fridge.domain.repository.ChatRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import okio.IOException
import org.json.JSONObject
import retrofit2.HttpException
import javax.inject.Inject

class ChatRepositoryImpl @Inject constructor(
    private val api: ChatApi,
    private val db: MessageItemDatabase,
    @ApplicationContext private val context: Context,
    @Dispatcher(AppDispatchers.IO) private val ioDispatcher: CoroutineDispatcher,
): ChatRepository {
    val dataStore = context.dataStore

    @WorkerThread
    override fun readChats(id: Long): Flow<Resource<Int>> = flow {
        emit(Resource.Loading())
        try {
            val response = api.readChats(id)
            if(response.isSuccess){
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

    @WorkerThread
    override fun joinChatRoom(id: Long): Flow<Resource<ChattingRoom>> = flow {
        emit(Resource.Loading())
        try {
            val response = api.joinChatRoom(id)
            if(response.isSuccess){
                emit(Resource.Success(response.result!!.toChattingRoom()))
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
    override fun closeChatRoom(id: Long): Flow<Resource<String>> = flow {
        emit(Resource.Loading())
        try {
            val response = api.closeChatRoom(id)
            if(response.isSuccess){
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

    @WorkerThread
    override fun enterChatRoom(id: Long): Flow<Resource<ChattingRoom>> = flow {
        emit(Resource.Loading())
        try {
            val response = api.enterChatRoom(id)
            if(response.isSuccess){
                emit(Resource.Success(response.result!!.toChattingRoom()))
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
    override fun checkWhoIsIn(id: Long): Flow<Resource<List<ChatMember>>> = flow {
        emit(Resource.Loading())
        try {
            Resource.Loading(data = null)
            val response = api.checkWhoIsIn(id)
            if(response.isSuccess){
                emit(Resource.Success(response.result?.map { it.toChatMember() }))
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
    override fun getMessages(id: Long, lastMessageId: Long?): Flow<Resource<List<Message>>> = flow {
        emit(Resource.Loading())
        try {
            Resource.Loading(data = null)
            val response = api.getMessages(id, lastMessageId)
            if(response.isSuccess){
                emit(Resource.Success(response.result?.map { it.toMessage() }))
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
    override fun getMyRooms(): Flow<Resource<List<ChattingRoomRaw>>> = flow {
        emit(Resource.Loading())
        try {
            val response = api.getMyRooms()
            if(response.isSuccess){
                emit(Resource.Success(response.result?.map { it.toChattingRoomRaw() }))
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
    override fun exitChatRoom(id: Long): Flow<Resource<String>> = flow {
        emit(Resource.Loading())
        try {
            Resource.Loading(data = null)
            val response = api.exitChatRoom(id)
            if(response.isSuccess){
                emit(Resource.Success(response.result))
                db.dao.clearMessages(id)
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

    @OptIn(ExperimentalPagingApi::class)
    @WorkerThread
    override fun getMessagePaged(roomId: Long): Flow<PagingData<MessageItemEntity>> {
        return Pager(
            config = PagingConfig(pageSize = MESSAGE_PAGE_SIZE, enablePlaceholders = false),
            remoteMediator = MessagePagingSource(roomId, api, db),
            pagingSourceFactory = { db.dao.getMessagesPaging(roomId) }
        ).flow.flowOn(ioDispatcher)
    }

    override suspend fun getUserId(): Long? {
        return dataStore.data.map { prefs ->
            prefs[USER_ID]
        }.first()
    }
}