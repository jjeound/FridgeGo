package com.example.untitled_capstone.data.repository

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
import kotlinx.coroutines.flow.Flow
import okio.IOException
import retrofit2.HttpException
import javax.inject.Inject

class HomeRepositoryImpl @Inject constructor(
    private val api: HomeApi,
    private val db: RecipeItemDatabase
): HomeRepository {
    override suspend fun getTastePreference(): Resource<TastePreference> {
        return try {
            Resource.Loading(data = null)
            val response = api.getTastePreference()
            if(response.isSuccess){
                Resource.Success(response.toDomainModel())
            }else {
                Resource.Error(message = response.toString())
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
}