package com.example.untitled_capstone.data.repository

import android.util.Log
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.untitled_capstone.core.util.Constants.NETWORK_PAGE_SIZE
import com.example.untitled_capstone.core.util.Resource
import com.example.untitled_capstone.data.local.db.FridgeItemDatabase
import com.example.untitled_capstone.data.local.entity.FridgeItemEntity
import com.example.untitled_capstone.data.pagination.FridgePagingSource
import com.example.untitled_capstone.data.remote.service.FridgeApi
import com.example.untitled_capstone.data.util.FridgeFetchType
import com.example.untitled_capstone.domain.model.FridgeItem
import com.example.untitled_capstone.domain.repository.FridgeRepository
import com.google.gson.Gson
import kotlinx.coroutines.flow.Flow
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import okio.IOException
import retrofit2.HttpException
import java.io.File


class FridgeRepositoryImpl(
    private val api: FridgeApi,
    private val db: FridgeItemDatabase,
): FridgeRepository {
    @OptIn(ExperimentalPagingApi::class)
    override fun getFridgeItemsPaged(fetchType: FridgeFetchType): Flow<PagingData<FridgeItemEntity>> {
        return Pager(
            config = PagingConfig(pageSize = NETWORK_PAGE_SIZE, enablePlaceholders = false),
            remoteMediator = FridgePagingSource(api, db, fetchType),
            pagingSourceFactory = { db.dao.getFridgeItems() }
        ).flow
    }

    @OptIn(ExperimentalPagingApi::class)
    override fun getFridgeItemsByDate(fetchType: FridgeFetchType): Flow<PagingData<FridgeItemEntity>> {
        return Pager(
            config = PagingConfig(pageSize = NETWORK_PAGE_SIZE, enablePlaceholders = false),
            remoteMediator = FridgePagingSource(api, db, fetchType),
            pagingSourceFactory = { db.dao.getFridgeItems() }
        ).flow
    }

    override suspend fun addItem(item: FridgeItem, image: File?): Resource<String> {
        return try {
            Resource.Loading(data = null)
            val json = Gson().toJson(item.toNewFridgeItemDto())
            val jsonBody = json.toRequestBody("application/json; charset=utf-8".toMediaType())
            val requestFile = image?.asRequestBody("image/*".toMediaTypeOrNull())
            val body = requestFile?.let {MultipartBody.Part.createFormData("ingredientImage", image.name, requestFile)}
            Log.d("tag", "addItem: ${item.toNewFridgeItemDto()}")

            val response = api.addFridgeItem(jsonBody, body)
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

    override suspend fun toggleNotification(
        id: Long,
        alarmStatus: Boolean
    ): Resource<String> {
        return try {
            Resource.Loading(data = null)
            val response = api.toggleNotification(id, alarmStatus)
            if(response.isSuccess){
                db.dao.toggleNotification(id, alarmStatus)
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

    override suspend fun modifyItem(updatedItem: FridgeItem): Resource<String> {
        return try {
            Resource.Loading(data = null)
            val response = api.modifyItem(updatedItem.id, updatedItem.toModifyFridgeReqDto())
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

    override suspend fun deleteItem(id: Long): Resource<String> {
        return try {
            Resource.Loading(data = null)
            val response = api.deleteItem(id)
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

    override suspend fun getFridgeItemById(id: Long): Resource<FridgeItem> {
        return try {
            Resource.Loading(data = null)
            val response = api.getFridgeItemById(id)
            if(response.isSuccess){
                Resource.Success(response.result!!.toFridgeItem())
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