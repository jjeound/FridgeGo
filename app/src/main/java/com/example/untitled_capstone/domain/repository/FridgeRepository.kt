package com.example.untitled_capstone.domain.repository

import androidx.paging.PagingData
import com.example.untitled_capstone.core.util.Resource
import com.example.untitled_capstone.data.remote.dto.ContentDto
import com.example.untitled_capstone.data.remote.dto.FridgeResponse
import com.example.untitled_capstone.domain.model.FridgeItem
import kotlinx.coroutines.flow.Flow

interface FridgeRepository{
    fun getFridgeItemsPaged(): Flow<PagingData<FridgeItem>>
    suspend fun addItem(item: ContentDto): Resource<FridgeResponse>
    suspend fun toggleNotification(id: Int): Resource<FridgeResponse>
    suspend fun modifyItem(updatedItem: ContentDto): Resource<FridgeResponse>
    suspend fun deleteItem(id: Int): Resource<FridgeResponse>
//    suspend fun getFridgeItemById(id: Int): Resource<ContentDto?>
}