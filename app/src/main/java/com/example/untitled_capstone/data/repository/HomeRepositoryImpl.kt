package com.example.untitled_capstone.data.repository

import android.content.Context
import androidx.annotation.WorkerThread
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.untitled_capstone.core.util.Resource
import com.example.untitled_capstone.data.local.db.RecipeItemDatabase
import com.example.untitled_capstone.data.local.entity.RecipeItemEntity
import com.example.untitled_capstone.data.pagination.RecipePagingSource
import com.example.untitled_capstone.data.remote.dto.PreferenceDto
import com.example.untitled_capstone.data.remote.dto.RecipeReqDto
import com.example.untitled_capstone.data.remote.service.HomeApi
import com.example.untitled_capstone.domain.model.TastePreference
import com.example.untitled_capstone.domain.repository.HomeRepository
import kotlinx.coroutines.flow.Flow
import okio.IOException
import retrofit2.HttpException
import javax.inject.Inject
import androidx.core.content.edit
import com.example.untitled_capstone.core.util.Constants.NETWORK_PAGE_SIZE
import com.example.untitled_capstone.core.util.Constants.TASTE_PREFERENCE
import com.example.untitled_capstone.data.AppDispatchers
import com.example.untitled_capstone.data.Dispatcher
import com.example.untitled_capstone.data.remote.dto.ModifyRecipeBody
import com.example.untitled_capstone.data.remote.dto.RecipeLikedDto
import com.example.untitled_capstone.domain.model.Recipe
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import okhttp3.MultipartBody

class HomeRepositoryImpl @Inject constructor(
    private val api: HomeApi,
    private val db: RecipeItemDatabase,
    @ApplicationContext context: Context,
    @Dispatcher(AppDispatchers.IO) private val ioDispatcher: CoroutineDispatcher,
): HomeRepository {

    private val prefs = context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)

    @WorkerThread
    override fun getTastePreference(): Flow<Resource<TastePreference>> = flow {
        emit(Resource.Loading())
        val cached = prefs.getString(TASTE_PREFERENCE, null)
        if (cached != null) {
            emit(Resource.Success(TastePreference(cached)))
        } else {
            try {
                val response = api.getTastePreference()
                if(response.isSuccess){
                    emit(Resource.Success(response.result!!.toTastePreference()))
                }else {
                    emit(Resource.Error(message = response.message))
                }
            } catch (e: IOException) {
                emit(Resource.Error(e.toString()))
            } catch (e: HttpException) {
                emit(Resource.Error(e.toString()))
            }
        }
    }.flowOn(ioDispatcher)

    @WorkerThread
    override fun setTastePreference(tastePreference: TastePreference): Flow<Resource<String>> = flow {
        emit(Resource.Loading())
        try {
            val response = api.setTastePreference(tastePreference.toPreferenceDto())
            if(response.isSuccess){
                prefs.edit { putString(TASTE_PREFERENCE, tastePreference.tastePreference) }
                emit(Resource.Success(response.result))
            }else {
                emit(Resource.Error(message = response.message))
            }
        } catch (e: IOException) {
            emit(Resource.Error(e.toString()))
        } catch (e: HttpException) {
            emit(Resource.Error(e.toString()))
        }
    }.flowOn(ioDispatcher)

    @WorkerThread
    @OptIn(ExperimentalPagingApi::class)
    override fun getRecipes(): Flow<PagingData<RecipeItemEntity>> {
        return Pager(
            config = PagingConfig(pageSize = NETWORK_PAGE_SIZE, enablePlaceholders = false),
            remoteMediator = RecipePagingSource(api, db),
            pagingSourceFactory = { db.dao.getRecipeItems() }
        ).flow.flowOn(ioDispatcher)
    }

    @WorkerThread
    override fun getRecipeById(id: Long): Flow<Resource<Recipe>> = flow {
        emit(Resource.Loading())
        try {
            val response = api.getRecipeById(id)
            if(response.isSuccess){
                emit(Resource.Success(response.result!!.toRecipe()))
            }else {
                emit(Resource.Error(message = response.message))
            }
        } catch (e: IOException) {
            emit(Resource.Error(e.toString()))
        } catch (e: HttpException) {
            emit(Resource.Error(e.toString()))
        }
    }.flowOn(ioDispatcher)

    @WorkerThread
    override fun toggleLike(id: Long, liked: Boolean): Flow<Resource<Boolean>> = flow {
        emit(Resource.Loading())
        try {
            val response = api.toggleLike(id)
            if(response.isSuccess){
                emit(Resource.Success(response.result!!.liked))
            }else {
                emit(Resource.Error(message = response.message))
            }
        } catch (e: IOException) {
            emit(Resource.Error(e.toString()))
        } catch (e: HttpException) {
            emit(Resource.Error(e.toString()))
        }
    }.flowOn(ioDispatcher)

    @WorkerThread
    override fun addRecipe(recipe: String): Flow<Resource<String>> = flow {
        emit(Resource.Loading())
        try {
            val response = api.addRecipe(RecipeReqDto(recipe))
            if(response.isSuccess){
                emit(Resource.Success(response.result))
            }else {
                emit(Resource.Error(message = response.message))
            }
        } catch (e: IOException) {
            emit(Resource.Error(e.toString()))
        } catch (e: HttpException) {
            emit(Resource.Error(e.toString()))
        }
    }.flowOn(ioDispatcher)

    @WorkerThread
    override fun deleteRecipe(id: Long): Flow<Resource<String>> = flow {
        emit(Resource.Loading())
        try {
            val response = api.deleteRecipe(id)
            if(response.isSuccess){
                emit(Resource.Success(response.result))
            }else {
                emit(Resource.Error(message = response.message))
            }
        } catch (e: IOException) {
            emit(Resource.Error(e.toString()))
        } catch (e: HttpException) {
            emit(Resource.Error(e.toString()))
        }
    }.flowOn(ioDispatcher)

    @WorkerThread
    override fun modifyRecipe(recipe: Recipe): Flow<Resource<String>> = flow {
        emit(Resource.Loading())
        try {
            val response = api.modifyRecipe(recipe.id, ModifyRecipeBody(title = recipe.title,
                instructions = recipe.instructions, ingredients = recipe.ingredients))
            if(response.isSuccess){
                emit(Resource.Success(response.result))
            }else {
                emit(Resource.Error(message = response.message))
            }
        } catch (e: IOException) {
            emit(Resource.Error(e.toString()))
        } catch (e: HttpException) {
            emit(Resource.Error(e.toString()))
        }
    }.flowOn(ioDispatcher)

    @WorkerThread
    override fun getFirstRecommendation(): Flow<Resource<String>> = flow {
        emit(Resource.Loading())
        try {
            val response = api.getFirstRecommendation()
            if(response.isSuccess){
                emit(Resource.Success(response.result!!.reply))
            }else {
                emit(Resource.Error(message = response.message))
            }
        } catch (e: IOException) {
            emit(Resource.Error(e.toString()))
        } catch (e: HttpException) {
            emit(Resource.Error(e.toString()))
        }
    }.flowOn(ioDispatcher)

    @WorkerThread
    override fun getAnotherRecommendation(): Flow<Resource<String>> = flow {
        emit(Resource.Loading())
        try {
            val response = api.getAnotherRecommendation()
            if(response.isSuccess){
                emit(Resource.Success(response.result!!.reply))
            }else {
                emit(Resource.Error(message = response.message))
            }
        } catch (e: IOException) {
            emit(Resource.Error(e.toString()))
        } catch (e: HttpException) {
            emit(Resource.Error(e.toString()))
        }
    }.flowOn(ioDispatcher)

    override fun isFirstSelection(): Boolean {
        return prefs.getBoolean("isFirstSelection", true)
    }

    override fun setFirstSelection(isFirst: Boolean) {
        prefs.edit { putBoolean("isFirstSelection", isFirst) }
    }

    @WorkerThread
    override fun uploadImage(id: Long, image: MultipartBody.Part): Flow<Resource<String>> = flow {
        emit(Resource.Loading())
        try {
            val response = api.uploadImage(id, image)
            if(response.isSuccess){
                emit(Resource.Success(response.result))
            }else {
                emit(Resource.Error(message = response.message))
            }
        } catch (e: IOException) {
            emit(Resource.Error(e.toString()))
        } catch (e: HttpException) {
            emit(Resource.Error(e.toString()))
        }
    }.flowOn(ioDispatcher)
}