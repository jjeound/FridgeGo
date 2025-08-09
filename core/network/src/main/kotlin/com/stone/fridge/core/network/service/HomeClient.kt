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
import javax.inject.Inject

class HomeClient @Inject constructor(
    private val homeApi: HomeApi,
) {
    suspend fun getTastePreference(): ApiResponse<Preference> =
        homeApi.getTastePreference()

    suspend fun setTastePreference(
        tastePreference: Preference
    ): ApiResponse<String> =
        homeApi.setTastePreference(tastePreference)

    suspend fun getRecipe(
        page: Int
    ): ApiResponse<RecipeResult> =
        homeApi.getRecipe(page, PAGING_SIZE)

    suspend fun getFirstRecommendation(): ApiResponse<ChatbotResult> =
        homeApi.getFirstRecommendation()

    suspend fun getAnotherRecommendation(): ApiResponse<ChatbotResult> =
        homeApi.getAnotherRecommendation()

    suspend fun deleteRecipe(
        recipeId: Long
    ): ApiResponse<String> =
        homeApi.deleteRecipe(recipeId)

    suspend fun addRecipe(
        recipe: RecipeReq
    ): ApiResponse<String> =
        homeApi.addRecipe(recipe)

    suspend fun getRecipeById(
        recipeId: Long
    ): ApiResponse<Recipe> =
        homeApi.getRecipeById(recipeId)

    suspend fun toggleLike(
        recipeId: Long,
    ): ApiResponse<RecipeLiked> =
        homeApi.toggleLike(recipeId)

    suspend fun modifyRecipe(
        recipeId: Long,
        recipe: ModifyRecipeBody
    ): ApiResponse<String> =
        homeApi.modifyRecipe(recipeId, recipe)

    suspend fun uploadImage(
        recipeId: Long,
        recipeImage: MultipartBody.Part
    ): ApiResponse<String> =
        homeApi.uploadImage(recipeId, recipeImage)

    companion object {
        private const val PAGING_SIZE = 10
    }
}