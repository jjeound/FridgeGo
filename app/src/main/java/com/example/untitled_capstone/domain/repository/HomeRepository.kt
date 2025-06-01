package com.example.untitled_capstone.domain.repository

import androidx.annotation.WorkerThread
import androidx.paging.PagingData
import com.example.untitled_capstone.core.util.Resource
import com.example.untitled_capstone.data.local.entity.RecipeItemEntity
import com.example.untitled_capstone.domain.model.Recipe
import com.example.untitled_capstone.domain.model.TastePreference
import kotlinx.coroutines.flow.Flow
import java.io.File

interface HomeRepository {
    @WorkerThread
    fun getTastePreference(): Flow<Resource<TastePreference>>
    @WorkerThread
    fun setTastePreference(tastePreference: TastePreference): Flow<Resource<String>>
    @WorkerThread
    fun getRecipes(): Flow<PagingData<RecipeItemEntity>>
    @WorkerThread
    fun getRecipeById(id: Long): Flow<Resource<Recipe>>
    @WorkerThread
    fun toggleLike(id: Long, liked: Boolean): Flow<Resource<Boolean>>
    @WorkerThread
    fun addRecipe(recipe: String): Flow<Resource<String>>
    @WorkerThread
    fun deleteRecipe(id: Long): Flow<Resource<String>>
    @WorkerThread
    fun modifyRecipe(recipe: Recipe): Flow<Resource<String>>
    @WorkerThread
    fun getFirstRecommendation(): Flow<Resource<String>>
    @WorkerThread
    fun getAnotherRecommendation(): Flow<Resource<String>>
    fun isFirstSelection(): Boolean
    fun setFirstSelection(isFirst: Boolean)
    @WorkerThread
    fun uploadImage(id: Long, image: File): Flow<Resource<String>>
}