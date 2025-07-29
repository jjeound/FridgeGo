package com.stone.fridge.data.repository

import android.content.Context
import androidx.annotation.WorkerThread
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.stone.fridge.core.util.Resource
import com.stone.fridge.data.local.db.RecipeItemDatabase
import com.stone.fridge.data.local.entity.RecipeItemEntity
import com.stone.fridge.data.pagination.RecipePagingSource
import com.stone.fridge.data.remote.dto.RecipeReqDto
import com.stone.fridge.data.remote.service.HomeApi
import com.stone.fridge.domain.model.TastePreference
import com.stone.fridge.domain.repository.HomeRepository
import kotlinx.coroutines.flow.Flow
import okio.IOException
import retrofit2.HttpException
import javax.inject.Inject
import androidx.core.content.edit
import com.stone.fridge.core.util.Constants.NETWORK_PAGE_SIZE
import com.stone.fridge.core.util.Constants.TASTE_PREFERENCE
import com.stone.fridge.data.AppDispatchers
import com.stone.fridge.data.Dispatcher
import com.stone.fridge.data.remote.dto.ModifyRecipeBody
import com.stone.fridge.domain.model.Recipe
import com.stone.fridge.data.util.ImageCompressor
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import org.json.JSONObject
import java.io.File

class HomeRepositoryImpl @Inject constructor(
    private val api: HomeApi,
    private val db: RecipeItemDatabase,
    @ApplicationContext private val context: Context,
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
                val errorMessage = try {
                    val errorJson = e.response()?.errorBody()?.string()
                    val errorObj = JSONObject(errorJson ?: "")
                    errorObj.getString("message")
                } catch (_: Exception) {
                    "알 수 없는 오류가 발생했어요."
                }
                emit(Resource.Error(errorMessage))
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
            val errorMessage = try {
                val errorJson = e.response()?.errorBody()?.string()
                val errorObj = JSONObject(errorJson ?: "")
                errorObj.getString("message")
            } catch (_: Exception) {
                "알 수 없는 오류가 발생했어요."
            }
            emit(Resource.Error(errorMessage))
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
            val errorMessage = try {
                val errorJson = e.response()?.errorBody()?.string()
                val errorObj = JSONObject(errorJson ?: "")
                errorObj.getString("message")
            } catch (_: Exception) {
                "알 수 없는 오류가 발생했어요."
            }
            emit(Resource.Error(errorMessage))
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
            val errorMessage = try {
                val errorJson = e.response()?.errorBody()?.string()
                val errorObj = JSONObject(errorJson ?: "")
                errorObj.getString("message")
            } catch (_: Exception) {
                "알 수 없는 오류가 발생했어요."
            }
            emit(Resource.Error(errorMessage))
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
            val errorMessage = try {
                val errorJson = e.response()?.errorBody()?.string()
                val errorObj = JSONObject(errorJson ?: "")
                errorObj.getString("message")
            } catch (_: Exception) {
                "알 수 없는 오류가 발생했어요."
            }
            emit(Resource.Error(errorMessage))
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
            val errorMessage = try {
                val errorJson = e.response()?.errorBody()?.string()
                val errorObj = JSONObject(errorJson ?: "")
                errorObj.getString("message")
            } catch (_: Exception) {
                "알 수 없는 오류가 발생했어요."
            }
            emit(Resource.Error(errorMessage))
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
            val errorMessage = try {
                val errorJson = e.response()?.errorBody()?.string()
                val errorObj = JSONObject(errorJson ?: "")
                errorObj.getString("message")
            } catch (_: Exception) {
                "알 수 없는 오류가 발생했어요."
            }
            emit(Resource.Error(errorMessage))
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
            val errorMessage = try {
                val errorJson = e.response()?.errorBody()?.string()
                val errorObj = JSONObject(errorJson ?: "")
                errorObj.getString("message")
            } catch (_: Exception) {
                "알 수 없는 오류가 발생했어요."
            }
            emit(Resource.Error(errorMessage))
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
            val errorMessage = try {
                val errorJson = e.response()?.errorBody()?.string()
                val errorObj = JSONObject(errorJson ?: "")
                errorObj.getString("message")
            } catch (_: Exception) {
                "알 수 없는 오류가 발생했어요."
            }
            emit(Resource.Error(errorMessage))
        }
    }.flowOn(ioDispatcher)

    override fun isFirstSelection(): Boolean {
        return prefs.getBoolean("isFirstSelection", true)
    }

    override fun setFirstSelection(isFirst: Boolean) {
        prefs.edit { putBoolean("isFirstSelection", isFirst) }
    }

    @WorkerThread
    override fun uploadImage(id: Long, image: File): Flow<Resource<String>> = flow {
        emit(Resource.Loading())
        val compressedFile = ImageCompressor.compressImage(context, image)
        val requestFile = compressedFile.asRequestBody("image/*".toMediaTypeOrNull())
        val body = MultipartBody.Part.createFormData("recipeImage", compressedFile.name, requestFile)
        try {
            val response = api.uploadImage(id, body)
            if(response.isSuccess){
                emit(Resource.Success(response.result))
            }else {
                emit(Resource.Error(message = response.message))
            }
        } catch (e: IOException) {
            emit(Resource.Error(e.toString()))
        } catch (e: HttpException) {
            val errorMessage = try {
                val errorJson = e.response()?.errorBody()?.string()
                val errorObj = JSONObject(errorJson ?: "")
                errorObj.getString("message")
            } catch (_: Exception) {
                "알 수 없는 오류가 발생했어요."
            }
            emit(Resource.Error(errorMessage))
        }
    }.flowOn(ioDispatcher)
}