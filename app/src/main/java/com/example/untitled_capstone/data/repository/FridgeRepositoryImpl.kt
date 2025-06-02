package com.example.untitled_capstone.data.repository

import android.content.Context
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
import com.example.untitled_capstone.data.util.ImageCompressor
import com.example.untitled_capstone.domain.model.FridgeItem
import com.example.untitled_capstone.domain.repository.FridgeRepository
import com.google.gson.Gson
import dagger.hilt.android.qualifiers.ApplicationContext
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
import org.json.JSONObject
import retrofit2.HttpException
import java.io.File
import javax.inject.Inject


class FridgeRepositoryImpl @Inject constructor(
    private val api: FridgeApi,
    private val db: FridgeItemDatabase,
    @ApplicationContext private val context: Context,
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
            val errorMessage = try {
                val errorJson = e.response()?.errorBody()?.string()
                val errorObj = JSONObject(errorJson ?: "")
                errorObj.getString("message")
            } catch (ex: Exception) {
                "알 수 없는 오류가 발생했어요."
            }
            emit(Resource.Error(errorMessage))
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
            val errorMessage = try {
                val errorJson = e.response()?.errorBody()?.string()
                val errorObj = JSONObject(errorJson ?: "")
                errorObj.getString("message")
            } catch (ex: Exception) {
                "알 수 없는 오류가 발생했어요."
            }
            emit(Resource.Error(errorMessage))
        }
    }.flowOn(ioDispatcher)

    @WorkerThread
    override fun modifyItem(updatedItem: FridgeItem, image: File?, isOriginalImageDeleted: Boolean): Flow<Resource<String>> = flow {
        emit(Resource.Loading())
        try {
            val json = Gson().toJson(updatedItem.toModifyFridgeReqDto())
            val jsonBody = json.toRequestBody("application/json; charset=utf-8".toMediaType())
            val imageFile = if (image != null){ // 수정한 이미지
                val compressedFile = ImageCompressor.compressImage(context, image)
                val requestFile = compressedFile.asRequestBody("image/*".toMediaTypeOrNull())
                MultipartBody.Part.createFormData("ingredientImage", compressedFile.name, requestFile)
            }  else { // 이미지가 없거나 기존 이미지
                null
            }
            val response = api.modifyItem(updatedItem.id, jsonBody, imageFile)
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
            } catch (ex: Exception) {
                "알 수 없는 오류가 발생했어요."
            }
            emit(Resource.Error(errorMessage))
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
            val errorMessage = try {
                val errorJson = e.response()?.errorBody()?.string()
                val errorObj = JSONObject(errorJson ?: "")
                errorObj.getString("message")
            } catch (ex: Exception) {
                "알 수 없는 오류가 발생했어요."
            }
            emit(Resource.Error(errorMessage))
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
             val errorMessage = try {
                 val errorJson = e.response()?.errorBody()?.string()
                 val errorObj = JSONObject(errorJson ?: "")
                 errorObj.getString("message")
             } catch (ex: Exception) {
                 "알 수 없는 오류가 발생했어요."
             }
             emit(Resource.Error(errorMessage))
         }
    }.flowOn(ioDispatcher)
}