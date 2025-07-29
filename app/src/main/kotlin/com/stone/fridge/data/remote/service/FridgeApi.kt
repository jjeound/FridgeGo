package com.stone.fridge.data.remote.service

import com.stone.fridge.data.remote.dto.ApiResponse
import com.stone.fridge.data.remote.dto.ContentDto
import com.stone.fridge.data.remote.dto.FridgeResultDto
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query

interface FridgeApi {

    @GET("/api/ingredient/createdAt")
    suspend fun getFridgeItems(
        @Query("page") page: Int = 1,
        @Query("size") size: Int = 10,
    ): ApiResponse<FridgeResultDto>

    @GET("/api/ingredient/useByDate")
    suspend fun getFridgeItemsByDate(
        @Query("page") page: Int = 1,
        @Query("size") size: Int = 10,
    ): ApiResponse<FridgeResultDto>

    @Multipart
    @POST("/api/ingredient")
    suspend fun addFridgeItem(
        @Part("ingredientDtoReq") ingredientDtoReq: RequestBody,
        @Part ingredientImage: MultipartBody.Part? = null
    ): ApiResponse<String>

    @Multipart
    @PATCH("/api/ingredient/{ingredientId}")
    suspend fun modifyItem(
        @Path("ingredientId") ingredientId: Long,
        @Part("ingredientDtoReq") ingredientDtoReq: RequestBody,
        @Part ingredientImage: MultipartBody.Part? = null
    ): ApiResponse<String>

    @DELETE("/api/ingredient/{ingredientId}")
    suspend fun deleteItem(
        @Path("ingredientId") ingredientId: Long
    ): ApiResponse<String>

    @PATCH("/api/ingredient/{ingredientId}/alarm")
    suspend fun toggleNotification(
        @Path("ingredientId") ingredientId: Long,
        @Query("alarmStatus") alarmStatus: Boolean
    ): ApiResponse<String>

    @GET("/api/ingredient/{ingredientId}")
    suspend fun getFridgeItemById(
        @Path("ingredientId") ingredientId: Long
    ): ApiResponse<ContentDto>

}