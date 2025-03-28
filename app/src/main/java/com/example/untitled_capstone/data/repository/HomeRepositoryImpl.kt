package com.example.untitled_capstone.data.repository

import android.content.Context
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.untitled_capstone.core.util.Resource
import com.example.untitled_capstone.data.local.db.RecipeItemDatabase
import com.example.untitled_capstone.data.local.entity.RecipeItemEntity
import com.example.untitled_capstone.data.pagination.RecipePagingSource
import com.example.untitled_capstone.data.remote.dto.ApiResponse
import com.example.untitled_capstone.data.remote.dto.PreferenceDto
import com.example.untitled_capstone.data.remote.dto.RecipeReqDto
import com.example.untitled_capstone.data.remote.service.HomeApi
import com.example.untitled_capstone.data.repository.FridgeRepositoryImpl.Companion.NETWORK_PAGE_SIZE
import com.example.untitled_capstone.domain.model.TastePreference
import com.example.untitled_capstone.domain.repository.HomeRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import okio.IOException
import retrofit2.HttpException
import javax.inject.Inject
import androidx.core.content.edit
import com.example.untitled_capstone.core.util.Constants.TASTE_PREFERENCE
import com.example.untitled_capstone.domain.model.RecipeRaw

class HomeRepositoryImpl @Inject constructor(
    private val api: HomeApi,
    private val db: RecipeItemDatabase,
    context: Context
): HomeRepository {

    private val prefs = context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)

    override suspend fun getTastePreference(): Resource<TastePreference> {
        return try {
            Resource.Loading(data = null)
            if(prefs.getString(TASTE_PREFERENCE, null) != null){
                return Resource.Success(TastePreference(prefs.getString(TASTE_PREFERENCE, null)!!))
            } else{
                val response = api.getTastePreference()
                if(response.isSuccess){
                    Resource.Success(response.toTastePreference())
                }else {
                    Resource.Error(message = response.toString())
                }
            }
        } catch (e: IOException) {
            Resource.Error(e.toString())
        } catch (e: HttpException) {
            Resource.Error(e.toString())
        }
    }

    override suspend fun setTastePreference(tastePreference: PreferenceDto): Resource<ApiResponse> {
        return try {
            Resource.Loading(data = null)
            val response = api.setTastePreference(tastePreference)
            if(response.isSuccess){
                prefs.edit { putString(TASTE_PREFERENCE, tastePreference.tastePreference) }
                Resource.Success(response)
            }else {
                Resource.Error(message = response.toString())
            }
        } catch (e: IOException) {
            Resource.Error(e.toString())
        } catch (e: HttpException) {
            Resource.Error(e.toString())
        }
    }

    @OptIn(ExperimentalPagingApi::class)
    override fun getRecipes(): Flow<PagingData<RecipeItemEntity>> {
        return Pager(
            config = PagingConfig(pageSize = NETWORK_PAGE_SIZE, enablePlaceholders = false),
            remoteMediator = RecipePagingSource(api, db),
            pagingSourceFactory = { db.dao.getRecipeItems() }
        ).flow
    }

    override suspend fun getRecipeById(id: Long): Resource<RecipeRaw> {
        return try {
            Resource.Loading(data = null)
            val response = api.getRecipeById(id)
            if(response.isSuccess){
                Resource.Success(response.result.toRecipe())
            }else {
                Resource.Error(message = response.toString())
            }
        } catch (e: IOException) {
            Resource.Error(e.toString())
        } catch (e: HttpException) {
            Resource.Error(e.toString())
        }
    }

    override suspend fun toggleLike(id: Long, liked: Boolean): Resource<ApiResponse> {
        return try {
            Resource.Loading(data = null)
            val response = api.toggleLike(id)
            if(response.isSuccess){
                db.dao.toggleLike(id, liked)
                Resource.Success(response)
            }else {
                Resource.Error(message = response.toString())
            }
        } catch (e: IOException) {
            Resource.Error(e.toString())
        } catch (e: HttpException) {
            Resource.Error(e.toString())
        }
    }


    override suspend fun addRecipe(
        title: String,
        instructions: String
    ): Resource<ApiResponse> {
        return try {
            Resource.Loading(data = null)
            val response = api.addRecipe(RecipeReqDto(title, instructions))
            if(response.isSuccess){
                Resource.Success(response)
            }else {
                Resource.Error(message = response.toString())
            }
        } catch (e: IOException) {
            Resource.Error(e.toString())
        } catch (e: HttpException) {
            Resource.Error(e.toString())
        }
    }

    override suspend fun getFirstRecommendation(): Resource<String> {
        return try {
            Resource.Loading(data = null)
            val response = api.getFirstRecommendation()
            if(response.isSuccess){
                Resource.Success(response.result.reply)
            }else {
                Resource.Error(message = response.toString())
            }
        } catch (e: IOException) {
            Resource.Error(e.toString())
        } catch (e: HttpException) {
            Resource.Error(e.toString())
        }
    }

    override suspend fun getAnotherRecommendation(): Resource<String> {
        return try {
            Resource.Loading(data = null)
            val response = api.getAnotherRecommendation()
            if(response.isSuccess){
                Resource.Success(response.result.reply)
            }else {
                Resource.Error(message = response.toString())
            }
        } catch (e: IOException) {
            Resource.Error(e.toString())
        } catch (e: HttpException) {
            Resource.Error(e.toString())
        }
    }

    override fun isFirstSelection(): Boolean {
        return prefs.getBoolean("isFirstSelection", true)
    }

    override fun setFirstSelection(isFirst: Boolean) {
        prefs.edit { putBoolean("isFirstSelection", isFirst) }
    }
}