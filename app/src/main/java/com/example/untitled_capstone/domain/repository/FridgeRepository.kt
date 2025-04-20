package com.example.untitled_capstone.domain.repository

import androidx.paging.PagingData
import com.example.untitled_capstone.core.util.Resource
import com.example.untitled_capstone.data.local.entity.FridgeItemEntity
import com.example.untitled_capstone.data.util.FridgeFetchType
import com.example.untitled_capstone.domain.model.FridgeItem
import kotlinx.coroutines.flow.Flow
import okhttp3.MultipartBody
import java.io.File

interface FridgeRepository{
    fun getFridgeItemsPaged(fetchType: FridgeFetchType): Flow<PagingData<FridgeItemEntity>>
    fun getFridgeItemsByDate(fetchType: FridgeFetchType): Flow<PagingData<FridgeItemEntity>>
    suspend fun addItem(item: FridgeItem, image: File?): Resource<String>
    suspend fun toggleNotification(id: Long, alarmStatus: Boolean): Resource<String>
    suspend fun modifyItem(updatedItem: FridgeItem): Resource<String>
    suspend fun deleteItem(id: Long): Resource<String>
    suspend fun getFridgeItemById(id: Long): Resource<FridgeItem>
}