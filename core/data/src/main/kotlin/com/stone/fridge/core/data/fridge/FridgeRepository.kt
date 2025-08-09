package com.stone.fridge.core.data.fridge

import androidx.annotation.WorkerThread
import androidx.paging.PagingData
import com.stone.fridge.core.data.util.Resource
import com.stone.fridge.core.model.Fridge
import com.stone.fridge.core.model.ModifyFridgeReq
import com.stone.fridge.core.model.NewFridge
import com.stone.fridge.core.paging.FridgeFetchType
import kotlinx.coroutines.flow.Flow
import java.io.File

interface FridgeRepository{
    @WorkerThread
    fun getFridgeItems(fetchType: FridgeFetchType): Flow<PagingData<Fridge>>
    @WorkerThread
    fun addItem(item: NewFridge, image: File?): Flow<String>
    @WorkerThread
    fun toggleNotification(id: Long, alarmStatus: Boolean): Flow<String>
    @WorkerThread
    fun modifyItem(id:Long, updatedItem: ModifyFridgeReq, image: File? = null): Flow<String>
    @WorkerThread
    fun deleteItem(id: Long): Flow<String>
    @WorkerThread
    fun getFridgeItemById(id: Long): Flow<Fridge>
}