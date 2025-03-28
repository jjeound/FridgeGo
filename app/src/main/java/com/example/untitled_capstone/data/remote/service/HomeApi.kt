package com.example.untitled_capstone.data.remote.service

import com.example.untitled_capstone.data.remote.dto.ApiResponse
import com.example.untitled_capstone.data.remote.dto.ChatbotResponse
import com.example.untitled_capstone.data.remote.dto.GetRecipeByIdResponse
import com.example.untitled_capstone.data.remote.dto.PreferenceDto
import com.example.untitled_capstone.data.remote.dto.PreferenceResponse
import com.example.untitled_capstone.data.remote.dto.RecipeReqDto
import com.example.untitled_capstone.data.remote.dto.RecipeResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface HomeApi {
    @GET("/api/user/taste-preference")
    suspend fun getTastePreference(): PreferenceResponse

    @POST("/api/user/taste-preference")
    suspend fun setTastePreference(
        @Body tastePreference: PreferenceDto
    ): ApiResponse

    @GET("/api/recipe")
    suspend fun getRecipe(
        @Query("page") page: Int = 1,
        @Query("size") size: Int = 10,
    ): RecipeResponse

    @POST("/api/recipe")
    suspend fun addRecipe(
        @Body recipe: RecipeReqDto
    ): ApiResponse

    @GET("/api/recipe/{recipeId}")
    suspend fun getRecipeById(
        @Path("recipeId") recipeId: Long
    ): GetRecipeByIdResponse

    @PATCH("/api/recipe/like/{recipeId}")
    suspend fun toggleLike(
        @Path("recipeId") ingredientId: Long,
    ): ApiResponse

    @GET("/api/chatbot/recommend")
    suspend fun getFirstRecommendation(): ChatbotResponse

    @GET("/api/chatbot/recommend/another")
    suspend fun getAnotherRecommendation(): ChatbotResponse

}