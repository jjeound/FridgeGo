package com.example.untitled_capstone.data.remote


import com.example.untitled_capstone.data.remote.dto.ContentDto
import com.example.untitled_capstone.data.remote.dto.FridgeDto
import com.example.untitled_capstone.data.remote.dto.FridgeResponse
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface Api {

    @GET("/api/ingredient/createdAt")
    suspend fun getFridgeItems(
        @Query("page") page: Int = 1,
        @Query("size") size: Int = 10,
    ): FridgeDto

    @POST("/api/ingredient")
    suspend fun addFridgeItem(
        @Body fridgeItem: ContentDto
    ): FridgeResponse

    @PATCH("/api/ingredient/{ingredientId}")
    suspend fun modifyItem(
        @Path("ingredientId") ingredientId: Int,
        @Body fridgeItem: ContentDto
    ): FridgeResponse

    @DELETE("/api/ingredient/{ingredientId}")
    suspend fun deleteItem(
        @Path("ingredientId") ingredientId: Int
    ): FridgeResponse

    @PATCH("/api/ingredient/{ingredientId}/alarm")
    suspend fun toggleNotification(
        @Path("ingredientId") ingredientId: Int
    ): FridgeResponse

//    @GET("/api/ingredient/{ingredientId}")
//    suspend fun getItemById(
//        @Path("ingredientId") ingredientId: Int
//    ): FridgeDto

    companion object{
        const val BASE_URL = "https://api/"
    }
}