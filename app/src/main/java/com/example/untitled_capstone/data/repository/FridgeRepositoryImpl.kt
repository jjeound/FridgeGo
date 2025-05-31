package com.example.untitled_capstone.data.repository

import androidx.annotation.WorkerThread
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.untitled_capstone.core.util.Constants.NETWORK_PAGE_SIZE
import com.example.untitled_capstone.core.util.Resource
import com.example.untitled_capstone.data.AppDispatchers
import com.example.untitled_capstone.data.Dispatcher
import com.example.untitled_capstone.data.local.db.FridgeItemDatabase
import com.example.untitled_capstone.data.local.entity.FridgeItemEntity
import com.example.untitled_capstone.data.pagination.FridgePagingSource
import com.example.untitled_capstone.data.remote.service.FridgeApi
import com.example.untitled_capstone.data.util.FridgeFetchType
import com.example.untitled_capstone.domain.model.FridgeItem
import com.example.untitled_capstone.domain.repository.FridgeRepository
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import okio.IOException
import retrofit2.HttpException
import java.io.File
import javax.inject.Inject


class FridgeRepositoryImpl @Inject constructor(
    private val api: FridgeApi,
    private val db: FridgeItemDatabase,
    @Dispatcher(AppDispatchers.IO) private val ioDispatcher: CoroutineDispatcher,
): FridgeRepository {
    @OptIn(ExperimentalPagingApi::class)
    override fun getFridgeItemsPaged(fetchType: FridgeFetchType): Flow<PagingData<FridgeItemEntity>> {
        return Pager(
            config = PagingConfig(pageSize = NETWORK_PAGE_SIZE, enablePlaceholders = false),
            remoteMediator = FridgePagingSource(api, db, fetchType),
            pagingSourceFactory = { db.dao.getFridgeItems() }
        ).flow.flowOn(ioDispatcher)
    }

    @OptIn(ExperimentalPagingApi::class)
    override fun getFridgeItemsByDate(fetchType: FridgeFetchType): Flow<PagingData<FridgeItemEntity>> {
        return Pager(
            config = PagingConfig(pageSize = NETWORK_PAGE_SIZE, enablePlaceholders = false),
            remoteMediator = FridgePagingSource(api, db, fetchType),
            pagingSourceFactory = { db.dao.getFridgeItems() }
        ).flow.flowOn(ioDispatcher)
    }

    override fun addItem(item: FridgeItem, image: File?): Flow<Resource<String>> = flow {
        emit(Resource.Loading())
        try {
            val json = Gson().toJson(item.toNewFridgeItemDto())
            val jsonBody = json.toRequestBody("application/json; charset=utf-8".toMediaType())
            val requestFile = image?.asRequestBody("image/*".toMediaTypeOrNull())
            val body = requestFile?.let {MultipartBody.Part.createFormData("ingredientImage", image.name, requestFile)}
            val response = api.addFridgeItem(jsonBody, body)
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

    @WorkerThread
    override fun toggleNotification(
        id: Long,
        alarmStatus: Boolean
    ): Flow<Resource<String>> = flow {
        emit(Resource.Loading())
        try {
            val response = api.toggleNotification(id, alarmStatus)
            if(response.isSuccess){
                db.dao.toggleNotification(id, alarmStatus)
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
    override fun modifyItem(updatedItem: FridgeItem, image: MultipartBody.Part?): Flow<Resource<String>> = flow {
        emit(Resource.Loading())
        try {
            val json = Gson().toJson(updatedItem.toModifyFridgeReqDto())
            val jsonBody = json.toRequestBody("application/json; charset=utf-8".toMediaType())
            val response = api.modifyItem(updatedItem.id, jsonBody, image)
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

    override fun deleteItem(id: Long): Flow<Resource<String>> = flow {
        emit(Resource.Loading())
        try {
            Resource.Loading(data = null)
            val response = api.deleteItem(id)
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

    @WorkerThread
    override fun getFridgeItemById(id: Long): Flow<Resource<FridgeItem>> = flow {
        emit(Resource.Loading())
         try {
            Resource.Loading(data = null)
            val response = api.getFridgeItemById(id)
            if(response.isSuccess){
                emit(Resource.Success(response.result!!.toFridgeItem()))
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