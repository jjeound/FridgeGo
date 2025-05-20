package com.example.untitled_capstone.domain.repository

import androidx.annotation.WorkerThread
import androidx.paging.PagingData
import com.example.untitled_capstone.core.util.Resource
import com.example.untitled_capstone.data.local.entity.RecipeItemEntity
import com.example.untitled_capstone.domain.model.Recipe
import com.example.untitled_capstone.domain.model.TastePreference
import kotlinx.coroutines.flow.Flow
import okhttp3.MultipartBody

interface HomeRepository {
    @WorkerThread
    suspend fun getTastePreference(): Flow<Resource<TastePreference>>
    @WorkerThread
    suspend fun setTastePreference(tastePreference: TastePreference): Flow<Resource<String>>
    @WorkerThread
    fun getRecipes(): Flow<PagingData<RecipeItemEntity>>
    @WorkerThread
    suspend fun getRecipeById(id: Long): Flow<Resource<Recipe>>
    @WorkerThread
    suspend fun toggleLike(id: Long, liked: Boolean): Flow<Resource<Boolean>>
    @WorkerThread
    suspend fun addRecipe(recipe: String): Flow<Resource<String>>
    @WorkerThread
    suspend fun deleteRecipe(id: Long): Flow<Resource<String>>
    @WorkerThread
    suspend fun modifyRecipe(recipe: Recipe): Flow<Resource<String>>
    @WorkerThread
    suspend fun getFirstRecommendation(): Flow<Resource<String>>
    @WorkerThread
    suspend fun getAnotherRecommendation(): Flow<Resource<String>>
    fun isFirstSelection(): Boolean
    fun setFirstSelection(isFirst: Boolean)
    @WorkerThread
    suspend fun uploadImage(id: Long, image: MultipartBody.Part): Flow<Resource<String>>
}