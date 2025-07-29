package com.stone.fridge.domain.repository

import androidx.annotation.WorkerThread
import androidx.paging.PagingData
import com.stone.fridge.core.util.Resource
import com.stone.fridge.data.local.entity.FridgeItemEntity
import com.stone.fridge.data.util.FridgeFetchType
import com.stone.fridge.domain.model.FridgeItem
import com.stone.fridge.domain.model.NewFridge
import kotlinx.coroutines.flow.Flow
import java.io.File

interface FridgeRepository{
    @WorkerThread
    fun getFridgeItemsPaged(fetchType: FridgeFetchType): Flow<PagingData<FridgeItemEntity>>
    @WorkerThread
    fun getFridgeItemsByDate(fetchType: FridgeFetchType): Flow<PagingData<FridgeItemEntity>>
    @WorkerThread
    fun addItem(item: NewFridge, image: File?): Flow<Resource<String>>
    @WorkerThread
    fun toggleNotification(id: Long, alarmStatus: Boolean): Flow<Resource<String>>
    @WorkerThread
    fun modifyItem(updatedItem: FridgeItem, image: File? = null): Flow<Resource<String>>
    @WorkerThread
    fun deleteItem(id: Long): Flow<Resource<String>>
    @WorkerThread
    fun getFridgeItemById(id: Long): Flow<Resource<FridgeItem>>
}