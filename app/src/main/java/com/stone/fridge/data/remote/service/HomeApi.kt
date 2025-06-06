package com.stone.fridge.data.remote.service

import com.stone.fridge.data.remote.dto.ApiResponse
import com.stone.fridge.data.remote.dto.ChatbotResultDto
import com.stone.fridge.data.remote.dto.ModifyRecipeBody
import com.stone.fridge.data.remote.dto.PreferenceDto
import com.stone.fridge.data.remote.dto.RecipeDto
import com.stone.fridge.data.remote.dto.RecipeLikedDto
import com.stone.fridge.data.remote.dto.RecipeReqDto
import com.stone.fridge.data.remote.dto.RecipeResultDto
import okhttp3.MultipartBody
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query

interface HomeApi {
    @GET("/api/user/taste-preference")
    suspend fun getTastePreference(): ApiResponse<PreferenceDto>

    @POST("/api/user/taste-preference")
    suspend fun setTastePreference(
        @Body tastePreference: PreferenceDto
    ): ApiResponse<String>

    @GET("/api/recipe")
    suspend fun getRecipe(
        @Query("page") page: Int = 1,
        @Query("size") size: Int = 10,
    ): ApiResponse<RecipeResultDto>

    @POST("/api/recipe")
    suspend fun addRecipe(
        @Body recipe: RecipeReqDto
    ): ApiResponse<String>

    @GET("/api/recipe/{recipeId}")
    suspend fun getRecipeById(
        @Path("recipeId") recipeId: Long
    ): ApiResponse<RecipeDto>

    @PATCH("/api/recipe/like/{recipeId}")
    suspend fun toggleLike(
        @Path("recipeId") ingredientId: Long,
    ): ApiResponse<RecipeLikedDto>

    @GET("/api/chatbot/recommend")
    suspend fun getFirstRecommendation(): ApiResponse<ChatbotResultDto>

    @GET("/api/chatbot/recommend/another")
    suspend fun getAnotherRecommendation(): ApiResponse<ChatbotResultDto>

    @DELETE("/api/recipe/{recipeId}")
    suspend fun deleteRecipe(
        @Path("recipeId") recipeId: Long
    ): ApiResponse<String>

    @PATCH("/api/recipe/{recipeId}")
    suspend fun modifyRecipe(
        @Path("recipeId") recipeId: Long,
        @Body recipe: ModifyRecipeBody
    ): ApiResponse<String>

    @Multipart
    @POST("/api/s3/update-recipe/{recipeId}")
    suspend fun uploadImage(
        @Path("recipeId") recipeId: Long,
        @Part recipeImage: MultipartBody.Part
    ): ApiResponse<String>
}