package com.example.untitled_capstone.domain.repository

import androidx.annotation.WorkerThread
import androidx.paging.PagingData
import com.example.untitled_capstone.core.util.Resource
import com.example.untitled_capstone.data.local.entity.FridgeItemEntity
import com.example.untitled_capstone.data.util.FridgeFetchType
import com.example.untitled_capstone.domain.model.FridgeItem
import kotlinx.coroutines.flow.Flow
import okhttp3.MultipartBody
import java.io.File

interface FridgeRepository{
    @WorkerThread
    fun getFridgeItemsPaged(fetchType: FridgeFetchType): Flow<PagingData<FridgeItemEntity>>
    @WorkerThread
    fun getFridgeItemsByDate(fetchType: FridgeFetchType): Flow<PagingData<FridgeItemEntity>>
    @WorkerThread
    fun addItem(item: FridgeItem, image: File?): Flow<Resource<String>>
    @WorkerThread
    fun toggleNotification(id: Long, alarmStatus: Boolean): Flow<Resource<String>>
    @WorkerThread
    fun modifyItem(updatedItem: FridgeItem, image: MultipartBody.Part? = null): Flow<Resource<String>>
    @WorkerThread
    fun deleteItem(id: Long): Flow<Resource<String>>
    @WorkerThread
    fun getFridgeItemById(id: Long): Flow<Resource<FridgeItem>>
}