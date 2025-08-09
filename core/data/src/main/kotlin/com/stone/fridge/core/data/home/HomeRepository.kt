package com.stone.fridge.core.data.home

import androidx.annotation.WorkerThread
import androidx.paging.PagingData
import com.stone.fridge.core.model.Preference
import com.stone.fridge.core.model.Recipe
import com.stone.fridge.core.model.RecipeRaw
import kotlinx.coroutines.flow.Flow
import java.io.File

interface HomeRepository {
    @WorkerThread
    fun getTastePreference(): Flow<Preference>
    @WorkerThread
    fun setTastePreference(preference: String): Flow<String>
    @WorkerThread
    fun getRecipes(): Flow<PagingData<RecipeRaw>>
    @WorkerThread
    fun getRecipeById(id: Long): Flow<Recipe>
    @WorkerThread
    fun toggleLike(id: Long, liked: Boolean): Flow<Boolean>
    @WorkerThread
    fun addRecipe(recipe: String): Flow<String>
    @WorkerThread
    fun deleteRecipe(id: Long): Flow<String>
    @WorkerThread
    fun modifyRecipe(recipe: Recipe): Flow<String>
    @WorkerThread
    fun getAIRecipe(): Flow<String>
    @WorkerThread
    fun uploadImage(id: Long, image: File): Flow<String>
}