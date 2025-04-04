package com.example.untitled_capstone.domain.repository

import androidx.paging.PagingData
import com.example.untitled_capstone.core.util.Resource
import com.example.untitled_capstone.data.local.entity.FridgeItemEntity
import com.example.untitled_capstone.data.remote.dto.ContentDto
import com.example.untitled_capstone.data.remote.dto.ApiResponse
import com.example.untitled_capstone.data.remote.dto.NewFridgeItemDto
import com.example.untitled_capstone.data.util.FridgeFetchType
import com.example.untitled_capstone.domain.model.FridgeItem
import kotlinx.coroutines.flow.Flow

interface FridgeRepository{
    fun getFridgeItemsPaged(fetchType: FridgeFetchType): Flow<PagingData<FridgeItemEntity>>
    fun getFridgeItemsByDate(fetchType: FridgeFetchType): Flow<PagingData<FridgeItemEntity>>
    suspend fun addItem(item: NewFridgeItemDto): Resource<ApiResponse>
    suspend fun toggleNotification(id: Long, alarmStatus: Boolean): Resource<ApiResponse>
    suspend fun modifyItem(updatedItem: ContentDto): Resource<ApiResponse>
    suspend fun deleteItem(id: Long): Resource<ApiResponse>
    suspend fun getFridgeItemById(id: Long): Resource<FridgeItem>
}