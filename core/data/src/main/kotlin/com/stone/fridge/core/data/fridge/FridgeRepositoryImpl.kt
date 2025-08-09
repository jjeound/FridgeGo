package com.stone.fridge.core.data.fridge

import android.content.Context
import androidx.annotation.WorkerThread
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.google.gson.Gson
import com.stone.fridge.core.data.util.ImageCompressor
import com.stone.fridge.core.database.dao.FridgeItemDao
import com.stone.fridge.core.database.model.toDomain
import com.stone.fridge.core.model.Fridge
import com.stone.fridge.core.model.ModifyFridgeReq
import com.stone.fridge.core.model.NewFridge
import com.stone.fridge.core.network.AppDispatchers
import com.stone.fridge.core.network.Dispatcher
import com.stone.fridge.core.network.service.FridgeClient
import com.stone.fridge.core.paging.FridgeFetchType
import com.stone.fridge.core.paging.FridgePagingSource
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import javax.inject.Inject


class FridgeRepositoryImpl @Inject constructor(
    private val fridgeClient: FridgeClient,
    private val mediatorFactory: FridgePagingSource.Factory,
    private val dao: FridgeItemDao,
    private val gson: Gson,
    @param:ApplicationContext private val context: Context,
    @param:Dispatcher(AppDispatchers.IO) private val ioDispatcher: CoroutineDispatcher,
): FridgeRepository {
    @OptIn(ExperimentalPagingApi::class)
    override fun getFridgeItems(fetchType: FridgeFetchType): Flow<PagingData<Fridge>> {
        return Pager(
            config = PagingConfig(pageSize = 20, enablePlaceholders = false),
            remoteMediator = mediatorFactory.create(fetchType),
            pagingSourceFactory = { dao.getFridgeItems() }
        ).flow.map { pagingData ->
            pagingData.map { it.toDomain() }
        }.flowOn(ioDispatcher)
    }

    @WorkerThread
    override fun addItem(item: NewFridge, image: File?): Flow<String> = flow {
        val json = gson.toJson(item)
        val jsonBody = json.toRequestBody("application/json; charset=utf-8".toMediaType())
        val compressedFile = image?.let{ ImageCompressor.compressImage(context, image)}
        val requestFile = compressedFile?.asRequestBody("image/*".toMediaTypeOrNull())
        val body = requestFile?.let {
            MultipartBody.Part.createFormData("ingredientImage", image.name, requestFile)
        }
        fridgeClient.addFridgeItem(jsonBody, body).result?.let {
            emit(it)
        }
    }.flowOn(ioDispatcher)

    @WorkerThread
    override fun toggleNotification(
        id: Long,
        alarmStatus: Boolean
    ): Flow<String> = flow {
        fridgeClient.toggleNotification(id, alarmStatus).result?.let {
            dao.toggleNotification(id, alarmStatus)
            emit(it)
        }
    }.flowOn(ioDispatcher)

    @WorkerThread
    override fun modifyItem(id: Long, updatedItem: ModifyFridgeReq, image: File?): Flow<String> = flow {
        val json = gson.toJson(updatedItem)
        val jsonBody = json.toRequestBody("application/json; charset=utf-8".toMediaType())
        val imageFile = if (image != null){ // 수정한 이미지
            val compressedFile = ImageCompressor.compressImage(context, image)
            val requestFile = compressedFile.asRequestBody("image/*".toMediaTypeOrNull())
            MultipartBody.Part.createFormData("ingredientImage", compressedFile.name, requestFile)
        }  else { // 이미지가 없거나 기존 이미지
            null
        }
        fridgeClient.modifyItem(id, jsonBody, imageFile).result?.let {
            emit(it)
        }
    }.flowOn(ioDispatcher)

    override fun deleteItem(id: Long): Flow<String> = flow {
        fridgeClient.deleteItem(id).result?.let {
            emit(it)
        }
    }.flowOn(ioDispatcher)

    @WorkerThread
    override fun getFridgeItemById(id: Long): Flow<Fridge> = flow {
        fridgeClient.getFridgeItemById(id).result?.let {
            emit(it)
        }
    }.flowOn(ioDispatcher)
}