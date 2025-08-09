package com.stone.fridge.core.data.home

import android.content.Context
import androidx.annotation.WorkerThread
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.paging.map
import com.stone.fridge.core.data.util.ImageCompressor
import com.stone.fridge.core.data.util.PrefKeys.IS_FIRST_SELECTION
import com.stone.fridge.core.data.util.PrefKeys.TASTE_PREFERENCE
import com.stone.fridge.core.database.model.RecipeItemEntity
import com.stone.fridge.core.database.model.toDomain
import com.stone.fridge.core.model.ModifyRecipeBody
import com.stone.fridge.core.model.Preference
import com.stone.fridge.core.model.Recipe
import com.stone.fridge.core.model.RecipeRaw
import com.stone.fridge.core.model.RecipeReq
import com.stone.fridge.core.network.AppDispatchers
import com.stone.fridge.core.network.Dispatcher
import com.stone.fridge.core.network.service.HomeClient
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File


class HomeRepositoryImpl @Inject constructor(
    private val homeClient: HomeClient,
    private val pager: Pager<Int, RecipeItemEntity>,
    private val dataStore: DataStore<Preferences>,
    @param:ApplicationContext private val context: Context,
    @param:Dispatcher(AppDispatchers.IO) private val ioDispatcher: CoroutineDispatcher,
): HomeRepository {
    @WorkerThread
    override fun getTastePreference(): Flow<Preference> = flow {
        dataStore.data.map { prefs ->
            prefs[TASTE_PREFERENCE]
        }.first()?.let {
            emit(Preference(it))
        } ?: run {
            homeClient.getTastePreference().result?.let {
                emit(it)
            }
        }
    }.flowOn(ioDispatcher)

    @WorkerThread
    override fun setTastePreference(preference: String): Flow<String> = flow {
        homeClient.setTastePreference(Preference(preference)).result?.let {
            dataStore.edit { prefs ->
                prefs[TASTE_PREFERENCE] = preference
            }
            emit(it)
        }
    }.flowOn(ioDispatcher)

    @WorkerThread
    @OptIn(ExperimentalPagingApi::class)
    override fun getRecipes(): Flow<PagingData<RecipeRaw>> {
        return pager.flow.map { pagingData ->
            pagingData.map { it.toDomain() }
        }.flowOn(ioDispatcher)
    }

    @WorkerThread
    override fun getRecipeById(id: Long): Flow<Recipe> = flow {
        homeClient.getRecipeById(id).result?.let {
            emit(it)
        }
    }.flowOn(ioDispatcher)

    @WorkerThread
    override fun toggleLike(id: Long, liked: Boolean): Flow<Boolean> = flow {
        homeClient.toggleLike(id).result?.let {
            emit(it.liked)
        }
    }.flowOn(ioDispatcher)

    @WorkerThread
    override fun addRecipe(recipe: String): Flow<String> = flow {
        homeClient.addRecipe(RecipeReq(recipe)).result?.let {
            emit(it)
        }
    }.flowOn(ioDispatcher)

    @WorkerThread
    override fun deleteRecipe(id: Long): Flow<String> = flow {
        homeClient.deleteRecipe(id).result?.let {
            emit(it)
        }
    }.flowOn(ioDispatcher)

    @WorkerThread
    override fun modifyRecipe(recipe: Recipe): Flow<String> = flow {
        homeClient.modifyRecipe(recipe.id, ModifyRecipeBody(
            title = recipe.title,
            instructions = recipe.instructions, ingredients = recipe.ingredients
        )).result?.let {
            emit(it)
        }
    }.flowOn(ioDispatcher)

    @WorkerThread
    private fun getFirstRecommendation(): Flow<String> = flow {
        homeClient.getFirstRecommendation().result?.let {
            setFirstSelection(isFirst = false)
            emit(it.reply)
        }
    }.flowOn(ioDispatcher)

    @WorkerThread
    private fun getAnotherRecommendation(): Flow<String> = flow {
        homeClient.getAnotherRecommendation().result?.let {
            emit(it.reply)
        }
    }.flowOn(ioDispatcher)

    @WorkerThread
    override fun getAIRecipe(): Flow<String> = isFirstSelection().flatMapLatest{ isFirst ->
        if (isFirst) {
            getFirstRecommendation()
        } else {
            getAnotherRecommendation()
        }
    }.flowOn(ioDispatcher)

    @WorkerThread
    private fun isFirstSelection(): Flow<Boolean> {
        return dataStore.data.map { preferences ->
            preferences[IS_FIRST_SELECTION] == true
        }.flowOn(ioDispatcher)
    }

    @WorkerThread
    private suspend fun setFirstSelection(isFirst: Boolean) {
        dataStore.edit { preferences ->
            preferences[IS_FIRST_SELECTION] = isFirst
        }
    }

    @WorkerThread
    override fun uploadImage(id: Long, image: File): Flow<String> = flow {
        val compressedFile = ImageCompressor.compressImage(context, image)
        val requestFile = compressedFile.asRequestBody("image/*".toMediaTypeOrNull())
        val body = MultipartBody.Part.createFormData("recipeImage", compressedFile.name, requestFile)
        homeClient.uploadImage(id, body).result?.let {
            emit(it)
        }
    }.flowOn(ioDispatcher)
}