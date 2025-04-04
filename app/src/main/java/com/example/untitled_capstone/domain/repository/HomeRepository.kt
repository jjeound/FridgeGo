package com.example.untitled_capstone.domain.repository

import androidx.paging.PagingData
import com.example.untitled_capstone.core.util.Resource
import com.example.untitled_capstone.data.local.entity.RecipeItemEntity
import com.example.untitled_capstone.data.remote.dto.ApiResponse
import com.example.untitled_capstone.data.remote.dto.PreferenceDto
import com.example.untitled_capstone.data.remote.dto.RecipeLikedDto
import com.example.untitled_capstone.domain.model.Recipe
import com.example.untitled_capstone.domain.model.TastePreference
import kotlinx.coroutines.flow.Flow
import okhttp3.MultipartBody

interface HomeRepository {
    suspend fun getTastePreference(): Resource<TastePreference>
    suspend fun setTastePreference(tastePreference: PreferenceDto): Resource<ApiResponse>
    fun getRecipes(): Flow<PagingData<RecipeItemEntity>>
    suspend fun getRecipeById(id: Long): Resource<Recipe>
    suspend fun toggleLike(id: Long, liked: Boolean): Resource<RecipeLikedDto>
    suspend fun addRecipe(recipe: String): Resource<ApiResponse>
    suspend fun deleteRecipe(id: Long): Resource<ApiResponse>
    suspend fun modifyRecipe(recipe: Recipe): Resource<ApiResponse>
    suspend fun getFirstRecommendation(): Resource<String>
    suspend fun getAnotherRecommendation(): Resource<String>
    fun isFirstSelection(): Boolean
    fun setFirstSelection(isFirst: Boolean)
    suspend fun uploadImage(id: Long, image: MultipartBody.Part): Resource<ApiResponse>
}