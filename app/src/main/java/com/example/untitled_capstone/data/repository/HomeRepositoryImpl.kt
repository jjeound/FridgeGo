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
import com.example.untitled_capstone.data.remote.dto.ModifyRecipeBody
import com.example.untitled_capstone.data.remote.dto.RecipeLikedDto
import com.example.untitled_capstone.domain.model.Recipe
import dagger.hilt.android.qualifiers.ApplicationContext
import okhttp3.MultipartBody

class HomeRepositoryImpl @Inject constructor(
    private val api: HomeApi,
    private val db: RecipeItemDatabase,
    @ApplicationContext context: Context
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
                    Resource.Success(response.result!!.toTastePreference())
                }else {
                    Resource.Error(message = response.message)
                }
            }
        } catch (e: IOException) {
            Resource.Error(e.toString())
        } catch (e: HttpException) {
            Resource.Error(e.toString())
        }
    }

    override suspend fun setTastePreference(tastePreference: TastePreference): Resource<String> {
        return try {
            Resource.Loading(data = null)
            val response = api.setTastePreference(tastePreference.toPreferenceDto())
            if(response.isSuccess){
                prefs.edit { putString(TASTE_PREFERENCE, tastePreference.tastePreference) }
                Resource.Success(response.result)
            }else {
                Resource.Error(message = response.message)
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

    override suspend fun getRecipeById(id: Long): Resource<Recipe> {
        return try {
            Resource.Loading(data = null)
            val response = api.getRecipeById(id)
            if(response.isSuccess){
                Resource.Success(response.result!!.toRecipe())
            }else {
                Resource.Error(message = response.message)
            }
        } catch (e: IOException) {
            Resource.Error(e.toString())
        } catch (e: HttpException) {
            Resource.Error(e.toString())
        }
    }

    override suspend fun toggleLike(id: Long, liked: Boolean): Resource<Boolean> {
        return try {
            Resource.Loading(data = null)
            val response = api.toggleLike(id)
            if(response.isSuccess){
                Resource.Success(response.result!!.liked)
            }else {
                Resource.Error(message = response.message)
            }
        } catch (e: IOException) {
            Resource.Error(e.toString())
        } catch (e: HttpException) {
            Resource.Error(e.toString())
        }
    }


    override suspend fun addRecipe(recipe: String): Resource<String> {
        return try {
            Resource.Loading(data = null)
            val response = api.addRecipe(RecipeReqDto(recipe))
            if(response.isSuccess){
                Resource.Success(response.result)
            }else {
                Resource.Error(message = response.message)
            }
        } catch (e: IOException) {
            Resource.Error(e.toString())
        } catch (e: HttpException) {
            Resource.Error(e.toString())
        }
    }

    override suspend fun deleteRecipe(id: Long): Resource<String> {
        return try {
            Resource.Loading(data = null)
            val response = api.deleteRecipe(id)
            if(response.isSuccess){
                Resource.Success(response.result)
            }else {
                Resource.Error(message = response.message)
            }
        } catch (e: IOException) {
            Resource.Error(e.toString())
        } catch (e: HttpException) {
            Resource.Error(e.toString())
        }
    }

    override suspend fun modifyRecipe(recipe: Recipe): Resource<String> {
        return try {
            Resource.Loading(data = null)
            val response = api.modifyRecipe(recipe.id, ModifyRecipeBody(title = recipe.title,
                instructions = recipe.instructions, ingredients = recipe.ingredients))
            if(response.isSuccess){
                Resource.Success(response.result)
            }else {
                Resource.Error(message = response.message)
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
                Resource.Success(response.result!!.reply)
            }else {
                Resource.Error(message = response.message)
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
                Resource.Success(response.result!!.reply)
            }else {
                Resource.Error(message = response.message)
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

    override suspend fun uploadImage(id: Long, image: MultipartBody.Part): Resource<String> {
        return try {
            Resource.Loading(data = null)
            val response = api.uploadImage(id, image)
            if(response.isSuccess){
                Resource.Success(response.result)
            }else {
                Resource.Error(message = response.message)
            }
        } catch (e: IOException) {
            Resource.Error(e.toString())
        } catch (e: HttpException) {
            Resource.Error(e.toString())
        }
    }
}