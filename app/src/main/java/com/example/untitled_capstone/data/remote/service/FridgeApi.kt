package com.example.untitled_capstone.data.remote.service

import com.example.untitled_capstone.data.remote.dto.ContentDto
import com.example.untitled_capstone.data.remote.dto.FridgeDto
import com.example.untitled_capstone.data.remote.dto.ApiResponse
import com.example.untitled_capstone.data.remote.dto.FridgeResponse
import com.example.untitled_capstone.data.remote.dto.ModifyFridgeReqDto
import com.example.untitled_capstone.data.remote.dto.NewFridgeItemDto
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface FridgeApi {

    @GET("/api/ingredient/createdAt")
    suspend fun getFridgeItems(
        @Query("page") page: Int = 1,
        @Query("size") size: Int = 10,
    ): FridgeDto

    @GET("/api/ingredient/useByDate")
    suspend fun getFridgeItemsByDate(
        @Query("page") page: Int = 1,
        @Query("size") size: Int = 10,
    ): FridgeDto

    @POST("/api/ingredient")
    suspend fun addFridgeItem(
        @Body fridgeItem: NewFridgeItemDto
    ): ApiResponse

    @PATCH("/api/ingredient/{ingredientId}")
    suspend fun modifyItem(
        @Path("ingredientId") ingredientId: Long,
        @Body fridgeItem: ModifyFridgeReqDto
    ): ApiResponse

    @DELETE("/api/ingredient/{ingredientId}")
    suspend fun deleteItem(
        @Path("ingredientId") ingredientId: Long
    ): ApiResponse

    @PATCH("/api/ingredient/{ingredientId}/alarm")
    suspend fun toggleNotification(
        @Path("ingredientId") ingredientId: Long,
        @Query("alarmStatus") alarmStatus: Boolean
    ): ApiResponse

    @GET("/api/ingredient/{ingredientId}")
    suspend fun getFridgeItemById(
        @Path("ingredientId") ingredientId: Long
    ): FridgeResponse

}