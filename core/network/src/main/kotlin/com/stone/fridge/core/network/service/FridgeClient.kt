package com.stone.fridge.core.network.service

import com.stone.fridge.core.model.Fridge
import com.stone.fridge.core.model.FridgeResult
import com.stone.fridge.core.network.model.ApiResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import javax.inject.Inject

class FridgeClient @Inject constructor(
    private val fridgeApi: FridgeApi,
) {
    suspend fun getFridgeItems(page: Int): ApiResponse<FridgeResult> =
        fridgeApi.getFridgeItems(page, PAGING_SIZE)

    suspend fun getFridgeItemsByDate(page: Int): ApiResponse<FridgeResult> =
        fridgeApi.getFridgeItemsByDate(page, PAGING_SIZE)

    suspend fun addFridgeItem(
        ingredientDtoReq: RequestBody,
        ingredientImage: MultipartBody.Part? = null,
    ): ApiResponse<String> =
        fridgeApi.addFridgeItem(ingredientDtoReq, ingredientImage)

    suspend fun modifyItem(
        ingredientId: Long,
        ingredientDtoReq: RequestBody,
        ingredientImage: MultipartBody.Part? = null,
    ): ApiResponse<String> =
        fridgeApi.modifyItem(ingredientId, ingredientDtoReq, ingredientImage)

    suspend fun deleteItem(ingredientId: Long): ApiResponse<String> =
        fridgeApi.deleteItem(ingredientId)

    suspend fun toggleNotification(
        ingredientId: Long,
        alarmStatus: Boolean,
    ): ApiResponse<String> =
        fridgeApi.toggleNotification(ingredientId, alarmStatus)

    suspend fun getFridgeItemById(ingredientId: Long): ApiResponse<Fridge> =
        fridgeApi.getFridgeItemById(ingredientId)

    companion object{
        private const val PAGING_SIZE = 10
    }
}