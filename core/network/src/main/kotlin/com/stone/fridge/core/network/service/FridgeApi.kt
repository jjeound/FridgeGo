package com.stone.fridge.core.network.service

import com.stone.fridge.core.model.Fridge
import com.stone.fridge.core.model.FridgeResult
import com.stone.fridge.core.network.model.ApiResponse
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
    ): ApiResponse<FridgeResult>

    @GET("/api/ingredient/useByDate")
    suspend fun getFridgeItemsByDate(
        @Query("page") page: Int = 1,
        @Query("size") size: Int = 10,
    ): ApiResponse<FridgeResult>

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
    ): ApiResponse<Fridge>
}