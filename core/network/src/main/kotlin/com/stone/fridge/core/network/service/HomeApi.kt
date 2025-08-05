package com.stone.fridge.core.network.service

import com.stone.fridge.core.model.ChatbotResult
import com.stone.fridge.core.model.ModifyRecipeBody
import com.stone.fridge.core.model.Preference
import com.stone.fridge.core.model.Recipe
import com.stone.fridge.core.model.RecipeLiked
import com.stone.fridge.core.model.RecipeReq
import com.stone.fridge.core.model.RecipeResult
import com.stone.fridge.core.network.model.ApiResponse
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
    suspend fun getTastePreference(): ApiResponse<Preference>

    @POST("/api/user/taste-preference")
    suspend fun setTastePreference(
        @Body tastePreference: Preference
    ): ApiResponse<String>

    @GET("/api/recipe")
    suspend fun getRecipe(
        @Query("page") page: Int = 1,
        @Query("size") size: Int = 10,
    ): ApiResponse<RecipeResult>

    @POST("/api/recipe")
    suspend fun addRecipe(
        @Body recipe: RecipeReq
    ): ApiResponse<String>

    @GET("/api/recipe/{recipeId}")
    suspend fun getRecipeById(
        @Path("recipeId") recipeId: Long
    ): ApiResponse<Recipe>

    @PATCH("/api/recipe/like/{recipeId}")
    suspend fun toggleLike(
        @Path("recipeId") ingredientId: Long,
    ): ApiResponse<RecipeLiked>

    @GET("/api/chatbot/recommend")
    suspend fun getFirstRecommendation(): ApiResponse<ChatbotResult>

    @GET("/api/chatbot/recommend/another")
    suspend fun getAnotherRecommendation(): ApiResponse<ChatbotResult>

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