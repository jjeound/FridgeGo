package com.example.untitled_capstone.domain.repository

import androidx.paging.PagingData
import com.example.untitled_capstone.core.util.Resource
import com.example.untitled_capstone.data.local.entity.FridgeItemEntity
import com.example.untitled_capstone.data.remote.dto.ContentDto
import com.example.untitled_capstone.data.remote.dto.FridgeResponse
import kotlinx.coroutines.flow.Flow

interface FridgeRepository{
    fun getFridgeItemsPaged(): Flow<PagingData<FridgeItemEntity>>
    suspend fun addItem(item: ContentDto): Resource<FridgeResponse>
    suspend fun toggleNotification(id: Int): Resource<FridgeResponse>
    suspend fun modifyItem(updatedItem: ContentDto): Resource<FridgeResponse>
    suspend fun deleteItem(id: Int): Resource<FridgeResponse>
    fun invalidatePagingSource()
//    suspend fun getFridgeItemById(id: Int): Resource<ContentDto?>
}